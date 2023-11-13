package com.jkhrs.jkhrsoi.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class Budget @JvmOverloads constructor(
    @PrimaryKey @ColumnInfo(name = "id") var id: Int,
    @ColumnInfo(name = "name") var name: String = "",
    @ColumnInfo(name = "getDate") var getDate: String = "",
    @ColumnInfo(name = "saldoAmount") var saldoAmount: String = "",
    @ColumnInfo(name = "accrualAmount") var accrualAmount: String = "",
    @ColumnInfo(name = "paymentAmount") var paymentAmount: String = ""
) {
    val budgetName : String
        get() = if (name.isNotEmpty()) name else ""

    val getDateText : String
        get() = if (getDate.isNotEmpty()) getDate else ""

    val saldoAmountText: String
        get() = if(saldoAmount.isNotEmpty()) saldoAmount else ""

    val accrualAmountText: String
        get() = if(accrualAmount.isNotEmpty()) accrualAmount else ""

    val paymentAmountText: String
        get() = if(paymentAmount.isNotEmpty()) paymentAmount else ""
}