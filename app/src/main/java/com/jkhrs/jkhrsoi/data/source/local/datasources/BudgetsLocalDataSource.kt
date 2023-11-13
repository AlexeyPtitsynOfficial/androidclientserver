package com.jkhrs.jkhrsoi.data.source.local.datasources

import com.jkhrs.jkhrsoi.data.Budget
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.source.datasources.BudgetsDataSource
import com.jkhrs.jkhrsoi.data.source.local.Daos.BudgetsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BudgetsLocalDataSource internal constructor(
    private val budgetsDao: BudgetsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BudgetsDataSource {
    override suspend fun getBudgets(id_region: Int): Result<List<Budget>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(budgetsDao.getBudgets())
        } catch (e: Exception) {
            Error(e)
        }
    }
}