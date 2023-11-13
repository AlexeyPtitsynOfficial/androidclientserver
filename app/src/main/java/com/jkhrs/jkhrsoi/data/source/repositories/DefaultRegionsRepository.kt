package com.jkhrs.jkhrsoi.data.source.repositories

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.source.datasources.RegionsDataSource
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.RegionsRepository
import com.jkhrs.jkhrsoi.di.ApplicationModule.RegionsRemoteDataSource
import com.jkhrs.jkhrsoi.di.ApplicationModule.RegionsLocalDataSource
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

class DefaultRegionsRepository @Inject constructor(
    @RegionsRemoteDataSource private val regionsRemoteDataSource: RegionsDataSource,
    @RegionsLocalDataSource private val regionsLocalDataSource: RegionsDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RegionsRepository {

    private var cachedRegions: ConcurrentMap<Int, Region>? = null

    override suspend fun getRegions(forceUpdate: Boolean): Result<List<Region>> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                // Respond immediately with cache if available and not dirty
                if (!forceUpdate) {
                    cachedRegions?.let { cachedTasks ->
                        return@withContext Success(cachedTasks.values.sortedBy { it.name })
                    }
                }

                val newRegions = fetchRegionsFromRemoteOrLocal(forceUpdate)

                // Refresh the cache with the new tasks
                (newRegions as? Success)?.let { refreshCache(it.data) }

                cachedRegions?.values?.let { tasks ->
                    return@withContext Success(tasks.sortedBy { it.name })
                }

                (newRegions as? Success)?.let {
                    if (it.data.isEmpty()) {
                        return@withContext Success(it.data)
                    }
                }

                return@withContext Error(Exception("Illegal state"))
            }
        }
    }

    private suspend fun fetchRegionsFromRemoteOrLocal(forceUpdate: Boolean): Result<List<Region>> {
        // Remote first
        val remoteRegions = regionsRemoteDataSource.getRegions()
        when (remoteRegions) {
            //is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteRegions.data)
                return remoteRegions
            }
            else -> Error(Exception("Ошибка при загрузке данных. Проверьте подключение к интернету"))
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localRegions = regionsLocalDataSource.getRegions()
        if (localRegions is Success) return localRegions
        return Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(tasks: List<Region>) {
        cachedRegions?.clear()
        tasks.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(regions: List<Region>) {
        //regionsLocalDataSource.deleteAllTasks()
        for (region in regions) {
            //regionsLocalDataSource.saveTask(region)
        }
    }

    private fun cacheRegion(region: Region): Region {
        val cachedRegion = Region(region.id, region.name, region.saldoAmount, region.accrualAmount, region.paymentAmount)
        // Create if it doesn't exist.
        if (cachedRegions == null) {
            cachedRegions = ConcurrentHashMap()
        }
        cachedRegions?.put(cachedRegion.id, cachedRegion)
        return cachedRegion
    }

    private inline fun cacheAndPerform(task: Region, perform: (Region) -> Unit) {
        val cachedTask = cacheRegion(task)
        perform(cachedTask)
    }

}