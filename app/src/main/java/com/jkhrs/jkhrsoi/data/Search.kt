package com.jkhrs.jkhrsoi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search")
data class Search @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "id") var id: Long,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "saldoAmount") var saldoAmount: String = "",
    @ColumnInfo(name = "accrualAmount") var accrualAmount: String = "",
    @ColumnInfo(name = "paymentAmount") var paymentAmount: String = ""
) {
    val resultName: String
        get() =if (name.isNotEmpty()) name else ""

    val saldoAmountText: String
        get() = if(saldoAmount.isNotEmpty()) saldoAmount else ""

    val accrualAmountText: String
        get() = if(accrualAmount.isNotEmpty()) accrualAmount else ""

    val paymentAmountText: String
        get() = if(paymentAmount.isNotEmpty()) paymentAmount else ""
}