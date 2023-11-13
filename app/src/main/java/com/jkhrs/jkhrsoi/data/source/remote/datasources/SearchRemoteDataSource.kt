package com.jkhrs.jkhrsoi.data.source.remote.datasources

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Search
import com.jkhrs.jkhrsoi.data.source.datasources.RegionsDataSource
import com.jkhrs.jkhrsoi.data.source.datasources.SearchDataSource
import com.jkhrs.jkhrsoi.data.source.remote.data.RegionRemote
import com.jkhrs.jkhrsoi.data.source.remote.data.SearchRemote
import com.jkhrs.jkhrsoi.util.APIError
import com.jkhrs.jkhrsoi.util.ErrorUtil
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object SearchRemoteDataSource :
    SearchDataSource {
    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private var SEARCH_SERVICE_DATA = LinkedHashMap<Long, Search>(2)

    val url = "http://api.jkhsakha.ru/opdata/"

    override suspend fun getSearchResult(searchText: String): Result<List<Search>> {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val rootObject= JSONObject()
        rootObject.put("search_text",searchText)

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WebService::class.java)

        val repos: Call<List<SearchRemote>> = service.getSearchResult(rootObject.toString())

        //val resultList  = repos?.execute()?.body()

        var resultList: List<SearchRemote?>? = null

        try {
            val response  = repos?.execute()
            if (response?.isSuccessful()!!) {
                resultList = response.body()!!
            } else {
                val apiError: APIError? = ErrorUtil.parseError(response)
                apiError?.getMessage()
            }
        } catch (e: IOException) {
            e.message
        }

        val sortedResultList = resultList?.sortedWith(compareBy({ it?.name }, { it?.name }))

        SEARCH_SERVICE_DATA.clear()
        for(result in sortedResultList!!) {
            addResult(
                result!!.id,
                result.name,
                result.saldoAmount,
                result.accrualAmount,
                result.paymentAmount
            )
        }

        val regions = SEARCH_SERVICE_DATA.values.toList()
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Success(regions)
    }

    private fun addResult(id: Long, name: String, saldoAmount: String, accrualAmount: String, paymentAmount: String) {
        val newResult = Search(id, name, saldoAmount, accrualAmount, paymentAmount)
        SEARCH_SERVICE_DATA.put(newResult.id, newResult)
    }
}