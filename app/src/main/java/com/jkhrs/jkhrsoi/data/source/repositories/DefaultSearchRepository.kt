package com.jkhrs.jkhrsoi.data.source.repositories

import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.Search
import com.jkhrs.jkhrsoi.data.source.datasources.SearchDataSource
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.SearchRepository
import com.jkhrs.jkhrsoi.di.ApplicationModule.SearchRemoteDataSource
import com.jkhrs.jkhrsoi.di.ApplicationModule.SearchLocalDataSource
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

class DefaultSearchRepository @Inject constructor(
    @SearchRemoteDataSource private val searchRemoteDataSource: SearchDataSource,
    @SearchLocalDataSource private val searchLocalDataSource: SearchDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchRepository {

    private var cachedSearch: ConcurrentMap<Long, Search>? = null

    override suspend fun getSearchResult(forceUpdate: Boolean, searchText: String): Result<List<Search>> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                // Respond immediately with cache if available and not dirty
                if (!forceUpdate) {
                    cachedSearch?.let { cachedTasks ->
                        return@withContext Success(cachedTasks.values.sortedBy { it.name })
                    }
                }

                val newSearch = fetchSearchFromRemoteOrLocal(forceUpdate, searchText)

                // Refresh the cache with the new tasks
                (newSearch as? Success)?.let { refreshCache(it.data) }

                cachedSearch?.values?.let { tasks ->
                    return@withContext Success(tasks.sortedBy { it.name })
                }

                (newSearch as? Success)?.let {
                    if (it.data.isEmpty()) {
                        return@withContext Success(it.data)
                    }
                }

                return@withContext Error(Exception("Illegal state"))
            }
        }
    }

    private suspend fun fetchSearchFromRemoteOrLocal(forceUpdate: Boolean, searchText: String): Result<List<Search>> {
        // Remote first
        val remoteSearch = searchRemoteDataSource.getSearchResult(searchText)
        when (remoteSearch) {
            //is Error -> Timber.w("Remote data source fetch failed")
            is Success -> {
                refreshLocalDataSource(remoteSearch.data)
                return remoteSearch
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localSearch = searchLocalDataSource.getSearchResult(searchText)
        if (localSearch is Success) return localSearch
        return Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(tasks: List<Search>) {
        cachedSearch?.clear()
        tasks.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(searchs: List<Search>) {
        //searchLocalDataSource.deleteAllTasks()
        for (search in searchs) {
            //searchLocalDataSource.saveTask(search)
        }
    }

    private fun cacheSearch(search: Search): Search {
        val cachedSearchs = Search(search.id, search.name, search.saldoAmount, search.accrualAmount, search.paymentAmount)
        // Create if it doesn't exist.
        if (cachedSearch == null) {
            cachedSearch = ConcurrentHashMap()
        }
        cachedSearch?.put(cachedSearchs.id, cachedSearchs)
        return cachedSearchs
    }

    private inline fun cacheAndPerform(task: Search, perform: (Search) -> Unit) {
        val cachedTask = cacheSearch(task)
        perform(cachedTask)
    }

}