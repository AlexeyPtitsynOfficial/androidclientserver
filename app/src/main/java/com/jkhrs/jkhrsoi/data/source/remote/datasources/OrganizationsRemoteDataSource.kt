package com.jkhrs.jkhrsoi.data.source.remote.datasources

import com.jkhrs.jkhrsoi.data.Organization
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.datasources.OrganizationsDataSource
import com.jkhrs.jkhrsoi.data.source.remote.data.OrganizationRemote
import kotlinx.coroutines.delay
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object OrganizationsRemoteDataSource : OrganizationsDataSource {

    private const val SERVICE_LATENCY_IN_MILLIS = 2000L

    private val url = "http://api.jkhsakha.ru/opdata/"

    private var ORGANIZATIONS_SERVICE_DATA = LinkedHashMap<Long, Organization>(2)

    override suspend fun getOrganizations(idRegion: Int, idBudget: Int): Result<List<Organization>> {

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

        val call: Call<List<OrganizationRemote>> = service.getOrganizations(rootObject.toString())

        val organizationList  = call.execute().body()

        val sortedOrganizationList = organizationList?.sortedWith(compareBy({ it.name }, { it.name }))

        ORGANIZATIONS_SERVICE_DATA.clear()
        for(organization in sortedOrganizationList!!) {
            addOrganization(
                organization.id,
                organization.name,
                organization.saldoAmount,
                organization.accrualAmount,
                organization.paymentAmount
            )
        }

        val organizations = ORGANIZATIONS_SERVICE_DATA.values.toList()
        delay(SERVICE_LATENCY_IN_MILLIS)
        return Result.Success(organizations)
    }

    private fun addOrganization(id: Long, name: String, saldoAmount: String, accrualAmount: String, paymentAmount: String) {
        val newOrganization = Organization(id, name, saldoAmount, accrualAmount, paymentAmount)
        ORGANIZATIONS_SERVICE_DATA.put(newOrganization.id, newOrganization)
    }
}