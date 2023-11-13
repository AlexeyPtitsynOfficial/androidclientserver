package com.jkhrs.jkhrsoi.data.source.repositories

import com.jkhrs.jkhrsoi.data.Municipal
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.source.datasources.MunicipalsDataSource
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.MunicipalsRepository
import com.jkhrs.jkhrsoi.di.ApplicationModule
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

class DefaultMunicipalsRepository @Inject constructor(
    @ApplicationModule.MunicipalsRemoteDataSource private val municipalsRemoteDataSource: MunicipalsDataSource,
    @ApplicationModule.MunicipalsLocalDataSource private val municipalsLocalDataSource: MunicipalsDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MunicipalsRepository {

    private var cachedMunicipals: ConcurrentMap<Int, Municipal>? = null

    override suspend fun getMunicipals(forceUpdate: Boolean,idRegion: Int, idBudget: Int): Result<List<Municipal>> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                // Respond immediately with cache if available and not dirty
                if (!forceUpdate) {
                    cachedMunicipals?.let { cachedTasks ->
                        return@withContext Success(cachedTasks.values.sortedBy { it.name })
                    }
                }

                val newMunicipals = fetchMunicipalsFromRemoteOrLocal(forceUpdate, idRegion, idBudget)

                // Refresh the cache with the new tasks
                (newMunicipals as? Success)?.let { refreshCache(it.data) }

                cachedMunicipals?.values?.let { tasks ->
                    return@withContext Success(tasks.sortedBy { it.name })
                }

                (newMunicipals as? Success)?.let {
                    if (it.data.isEmpty()) {
                        return@withContext Success(it.data)
                    }
                }

                return@withContext Error(Exception("Illegal state"))
            }
        }
    }

    private suspend fun fetchMunicipalsFromRemoteOrLocal(forceUpdate: Boolean,idRegion: Int, idBudget: Int): Result<List<Municipal>> {
        // Remote first
        val remoteMunicipals = municipalsRemoteDataSource.getMunicipals(idRegion, idBudget)
        when (remoteMunicipals) {
            //is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteMunicipals.data)
                return remoteMunicipals
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localMunicipals = municipalsLocalDataSource.getMunicipals(idRegion, idBudget)
        if (localMunicipals is Success) return localMunicipals
        return Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(tasks: List<Municipal>) {
        cachedMunicipals?.clear()
        tasks.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(municipals: List<Municipal>) {
        //municipalsLocalDataSource.deleteAllTasks()
        for (municipal in municipals) {
            //municipalsLocalDataSource.saveTask(municipal)
        }
    }

    private fun cacheMunicipal(municipal: Municipal): Municipal {
        val cachedMunicipal = Municipal(municipal.id, municipal.name, municipal.saldoAmount, municipal.accrualAmount, municipal.paymentAmount)
        // Create if it doesn't exist.
        if (cachedMunicipals == null) {
            cachedMunicipals = ConcurrentHashMap()
        }
        cachedMunicipals?.put(cachedMunicipal.id, cachedMunicipal)
        return cachedMunicipal
    }

    private inline fun cacheAndPerform(task: Municipal, perform: (Municipal) -> Unit) {
        val cachedTask = cacheMunicipal(task)
        perform(cachedTask)
    }

}