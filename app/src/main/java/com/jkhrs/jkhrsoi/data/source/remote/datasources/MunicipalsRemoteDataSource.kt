package com.jkhrs.jkhrsoi.data.source.remote.datasources

import com.jkhrs.jkhrsoi.data.Municipal
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.datasources.MunicipalsDataSource
import com.jkhrs.jkhrsoi.data.source.remote.data.MunicipalRemote
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object MunicipalsRemoteDataSource : MunicipalsDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private val client = OkHttpClient()
    private val url = "http://api.jkhsakha.ru/opdata/"

    private var MUNICIPALS_SERVICE_DATA = LinkedHashMap<Int, Municipal>(2)

    override suspend fun getMunicipals(idRegion: Int, idBudget: Int): Result<List<Municipal>> {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val rootObject= JSONObject()
        rootObject.put("id_region",idRegion)
        rootObject.put("id_budget",idBudget)

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WebService::class.java)

        val call: Call<List<MunicipalRemote>> = service.getMunicipals(rootObject.toString())

        val responseList  = call.execute().body()

        val sortedResponseList = responseList?.sortedWith(compareBy({ it.name }, { it.name }))

        MUNICIPALS_SERVICE_DATA.clear()
        for(municipal in sortedResponseList!!) {
            addMunicipal(
                municipal.id,
                municipal.name,
                municipal.saldoAmount,
                municipal.accrualAmount,
                municipal.paymentAmount
            )
        }

        val municipals = MUNICIPALS_SERVICE_DATA.values.toList()
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(municipals)
    }

    private fun addMunicipal(id: Int, name: String, saldoAmount: String, accrualAmount: String, paymentAmount: String) {
        val newMunicipal = Municipal(id, name, saldoAmount, accrualAmount, paymentAmount)
        MUNICIPALS_SERVICE_DATA.put(newMunicipal.id, newMunicipal)
    }

}