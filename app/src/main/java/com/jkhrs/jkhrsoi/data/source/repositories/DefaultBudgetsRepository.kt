package com.jkhrs.jkhrsoi.data.source.repositories

import com.jkhrs.jkhrsoi.data.Budget
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.source.datasources.BudgetsDataSource
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.BudgetsRepository
import com.jkhrs.jkhrsoi.di.ApplicationModule
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

class DefaultBudgetsRepository @Inject constructor(
    @ApplicationModule.BudgetsRemoteDataSource private val budgetsRemoteDataSource: BudgetsDataSource,
    @ApplicationModule.BudgetsLocalDataSource private val budgetsLocalDataSource: BudgetsDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : BudgetsRepository {

    private var cachedBudgets: ConcurrentMap<Int, Budget>? = null

    override suspend fun getBudgets(forceUpdate: Boolean, idRegion: Int): Result<List<Budget>> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                // Respond immediately with cache if available and not dirty
                if (!forceUpdate) {
                    cachedBudgets?.let { cachedTasks ->
                        return@withContext Success(cachedTasks.values.sortedBy { it.name })
                    }
                }

                val newBudgets = fetchBudgetsFromRemoteOrLocal(forceUpdate, idRegion)

                // Refresh the cache with the new tasks
                (newBudgets as? Success)?.let { refreshCache(it.data) }

                cachedBudgets?.values?.let { tasks ->
                    return@withContext Success(tasks.sortedBy { it.name })
                }

                (newBudgets as? Success)?.let {
                    if (it.data.isEmpty()) {
                        return@withContext Result.Success(it.data)
                    }
                }

                return@withContext Result.Error(Exception("Illegal state"))
            }
        }
    }

    private suspend fun fetchBudgetsFromRemoteOrLocal(forceUpdate: Boolean, idRegion: Int): Result<List<Budget>> {
        // Remote first
        val remoteBudgets = budgetsRemoteDataSource.getBudgets(idRegion)
        when (remoteBudgets) {
            //is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteBudgets.data)
                return remoteBudgets
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localBudgets = budgetsLocalDataSource.getBudgets(idRegion)
        if (localBudgets is Success) return localBudgets
        return Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(tasks: List<Budget>) {
        cachedBudgets?.clear()
        tasks.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(budgets: List<Budget>) {
        //budgetsLocalDataSource.deleteAllTasks()
        for (budget in budgets) {
            //budgetsLocalDataSource.saveTask(budget)
        }
    }

    private fun cacheBudget(budget: Budget): Budget {
        val cachedBudget = Budget(budget.id, budget.name, budget.getDate, budget.saldoAmount, budget.accrualAmount, budget.paymentAmount)
        // Create if it doesn't exist.
        if (cachedBudgets == null) {
            cachedBudgets = ConcurrentHashMap()
        }
        cachedBudgets?.put(cachedBudget.id, cachedBudget)
        return cachedBudget
    }

    private inline fun cacheAndPerform(task: Budget, perform: (Budget) -> Unit) {
        val cachedTask = cacheBudget(task)
        perform(cachedTask)
    }
}