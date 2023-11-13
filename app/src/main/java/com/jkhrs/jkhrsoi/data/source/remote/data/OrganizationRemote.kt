package com.jkhrs.jkhrsoi.data.source.remote.data

import com.google.gson.annotations.SerializedName

data class OrganizationRemote (
    @SerializedName("id") val id : Long,
    @SerializedName("name") val name : String,
    @SerializedName("saldoAmount") val saldoAmount: String,
    @SerializedName("accrualAmount") val accrualAmount: String,
    @SerializedName("paymentAmount") val paymentAmount: String
)