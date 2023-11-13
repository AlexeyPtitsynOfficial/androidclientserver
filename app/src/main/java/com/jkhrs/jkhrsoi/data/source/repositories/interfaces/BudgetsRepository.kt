package com.jkhrs.jkhrsoi.data.source.repositories.interfaces

import com.jkhrs.jkhrsoi.data.Budget
import com.jkhrs.jkhrsoi.data.Result

interface BudgetsRepository {

    suspend fun getBudgets(forceUpdate: Boolean = false, idRegion: Int): Result<List<Budget>>
}