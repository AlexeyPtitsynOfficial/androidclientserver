package com.jkhrs.jkhrsoi.data.source.datasources

import com.jkhrs.jkhrsoi.data.Budget
import com.jkhrs.jkhrsoi.data.Result

interface BudgetsDataSource {
    suspend fun getBudgets(id_region: Int): Result<List<Budget>>
}