package com.jkhrs.jkhrsoi.data.source.remote.datasources

import com.google.gson.Gson
import com.jkhrs.jkhrsoi.data.City
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.datasources.CitiesDataSource
import com.jkhrs.jkhrsoi.data.source.remote.data.CityRemote
import kotlinx.coroutines.delay
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object CitiesRemoteDataSource :CitiesDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private val client = OkHttpClient()
    private val url = "http://api.jkhsakha.ru/opdata/getcities"

    private var CITIES_SERVICE_DATA = LinkedHashMap<Int, City>(2)

    override suspend fun getCities(idRegion: Int, idBudget: Int, idMunicipal: Int): Result<List<City>> {

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val rootObject= JSONObject()
        rootObject.put("id_region",idRegion)
        rootObject.put("id_budget",idBudget)
        rootObject.put("id_municipal",idMunicipal)

        val retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(RegionsRemoteDataSource.url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WebService::class.java)

        val call: retrofit2.Call<List<CityRemote>> = service.getCities(rootObject.toString())



        try {
            val responseList  = call.execute().body()

            val sortedResponseList = responseList?.sortedWith(compareBy({ it.name }, { it.name }))

            CITIES_SERVICE_DATA.clear()
            for (city in sortedResponseList!!) {
                addCity(
                    city.id,
                    city.name,
                    city.saldoAmount,
                    city.accrualAmount,
                    city.paymentAmount
                )
            }

            val cities = CITIES_SERVICE_DATA.values.toList()
            delay(SERVICE_LATENCY_IN_MILLIS)
            return Result.Success(cities)
        }
        catch (e: IOException) {
            val error = e.message
            return Result.Error(e)
        }
    }

    private fun addCity(id: Int, name: String, saldoAmount: String, accrualAmount: String, paymentAmount: String) {
        val newCity = City(id, name, saldoAmount, accrualAmount, paymentAmount)
        CITIES_SERVICE_DATA.put(newCity.id, newCity)
    }
}