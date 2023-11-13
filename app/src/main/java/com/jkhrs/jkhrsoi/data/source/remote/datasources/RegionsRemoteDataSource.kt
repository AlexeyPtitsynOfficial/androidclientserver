package com.jkhrs.jkhrsoi.data.source.remote.datasources

import android.widget.Toast
import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.source.datasources.RegionsDataSource
import com.jkhrs.jkhrsoi.data.source.remote.data.RegionRemote
import com.jkhrs.jkhrsoi.util.APIError
import com.jkhrs.jkhrsoi.util.ErrorUtil
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import org.acra.ACRA
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object RegionsRemoteDataSource :
    RegionsDataSource {
    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private var REGIONS_SERVICE_DATA = LinkedHashMap<Int, Region>(2)

    val url = "http://api.jkhsakha.ru/opdata/"

    override suspend fun getRegions(): Result<List<Region>> {
        /*client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val gson = Gson()
            val  regionList: List<RegionRemote> = gson.fromJson(response.body?.string(), Array<RegionRemote>::class.java).toList()

            val sortedRegionList = regionList.sortedWith(compareBy({ it.name }, { it.name }))

            for(region in sortedRegionList) {
                addRegion(
                    region.id,
                    region.name,
                    region.deptAmount + " тыс.р.",
                    region.accrualAmount + " тыс.р."
                )
            }

            println(response.body!!.string())
        }*/
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WebService::class.java)

        val repos: Call<List<RegionRemote?>?>? = service.getRegions()

        //val regionList  = repos?.execute()?.body()

        var regionList: List<RegionRemote?>? = null

        try {
            val response  = repos?.execute()
            if (response?.isSuccessful()!!) {
                regionList = response.body()!!
            } else {
                val apiError: APIError? = ErrorUtil.parseError(response)
                apiError?.getMessage()
            }

            val sortedRegionList = regionList?.sortedWith(compareBy({ it?.name }, { it?.name }))

            REGIONS_SERVICE_DATA.clear()
            for(region in sortedRegionList!!) {
                addRegion(
                    region!!.id,
                    region.name,
                    region.saldoAmount,
                    region.accrualAmount,
                    region.paymentAmount
                )
            }

            val regions = REGIONS_SERVICE_DATA.values.toList()
            delay(SERVICE_LATENCY_IN_MILLIS)
            return Success(regions)
        } catch (e: IOException) {
            val error = e.message
            ACRA.getErrorReporter().handleException(e);
            return Error(e)
        }




    }

    private fun addRegion(id: Int, name: String, saldoAmount: String, accrualAmount: String, paymentAmount: String) {
        val newRegion = Region(id, name, saldoAmount, accrualAmount, paymentAmount)
        REGIONS_SERVICE_DATA.put(newRegion.id, newRegion)
    }
}