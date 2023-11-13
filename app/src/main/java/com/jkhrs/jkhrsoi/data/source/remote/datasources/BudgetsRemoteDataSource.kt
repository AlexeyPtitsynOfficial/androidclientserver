package com.jkhrs.jkhrsoi.data.source.remote.datasources

import com.jkhrs.jkhrsoi.data.Budget
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.datasources.BudgetsDataSource
import com.jkhrs.jkhrsoi.data.source.remote.data.BudgetRemote
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object BudgetsRemoteDataSource : BudgetsDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private val url = "http://api.jkhsakha.ru/opdata/"

    private var BUDGETS_SERVICE_DATA = LinkedHashMap<Int, Budget>(2)

    override suspend fun getBudgets(id_region: Int): Result<List<Budget>> {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val rootObject= JSONObject()
            rootObject.put("id_region",id_region)

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(RegionsRemoteDataSource.url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WebService::class.java)

        val call: Call<List<BudgetRemote>> = service.getBudgets(rootObject.toString())



        try {
            val budgetList  = call.execute().body()

            val sortedBudgetList = budgetList?.sortedWith(compareBy({ it.id }, { it.id }))

            BUDGETS_SERVICE_DATA.clear()

            for (budget in sortedBudgetList!!) {
                addBudget(
                    budget.id,
                    budget.name,
                    budget.getDate,
                    budget.saldoAmount,
                    budget.accrualAmount,
                    budget.paymentAmount
                )
            }

            val budgets = BUDGETS_SERVICE_DATA.values.toList()
            delay(SERVICE_LATENCY_IN_MILLIS)
            return Result.Success(budgets)

        }   catch (e: IOException) {
            val error = e.message
            return Result.Error(e)
        }
    }

    private fun addBudget(id: Int, name: String, getDate: String, saldoAmount: String, accrualAmount: String, paymentAmount: String) {
        val newBudget = Budget(id, name, getDate, saldoAmount, accrualAmount, paymentAmount)
        BUDGETS_SERVICE_DATA.put(newBudget.id, newBudget)
    }
}