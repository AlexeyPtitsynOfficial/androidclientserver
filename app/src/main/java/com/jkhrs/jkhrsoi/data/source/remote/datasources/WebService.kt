package com.jkhrs.jkhrsoi.data.source.remote.datasources

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.source.remote.data.*
import retrofit2.Call
import retrofit2.http.*


public interface WebService {

    @GET("getregions")
    fun getRegions(): Call<List<RegionRemote?>?>?

    @Headers("Content-Type: application/json")
    @POST("getbudgets")
    fun getBudgets(@Body  body : String): Call<List<BudgetRemote>>

    @Headers("Content-Type: application/json")
    @POST("getorganizations")
    fun getOrganizations(@Body  body : String): Call<List<OrganizationRemote>>

    @Headers("Content-Type: application/json")
    @POST("getmunicipals")
    fun getMunicipals(@Body  body : String): Call<List<MunicipalRemote>>

    @Headers("Content-Type: application/json")
    @POST("getcities")
    fun getCities(@Body  body : String): Call<List<CityRemote>>

    @Headers("Content-Type: application/json")
    @POST("search")
    fun getSearchResult(@Body  body : String): Call<List<SearchRemote>>
}