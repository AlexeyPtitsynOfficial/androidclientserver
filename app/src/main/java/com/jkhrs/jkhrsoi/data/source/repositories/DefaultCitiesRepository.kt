package com.jkhrs.jkhrsoi.data.source.repositories

import com.jkhrs.jkhrsoi.data.City
import com.jkhrs.jkhrsoi.data.Organization
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.datasources.CitiesDataSource
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.CitiesRepository
import com.jkhrs.jkhrsoi.di.ApplicationModule
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

class DefaultCitiesRepository @Inject constructor(
    @ApplicationModule.CitiesRemoteDataSource private val citiesRemoteDataSource: CitiesDataSource,
    @ApplicationModule.CitiesLocalDataSource private val citiesLocalDataSource: CitiesDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : CitiesRepository {

    private var cachedCities: ConcurrentMap<Int, City>? = null

    override suspend fun getCities(forceUpdate: Boolean,idRegion: Int, idBudget: Int, idMunicipal : Int): Result<List<City>> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                // Respond immediately with cache if available and not dirty
                if (!forceUpdate) {
                    cachedCities?.let { cachedTasks ->
                        return@withContext Result.Success(cachedTasks.values.sortedBy { it.name })
                    }
                }

                val newCities = fetchCitiesFromRemoteOrLocal(forceUpdate, idRegion, idBudget, idMunicipal)

                // Refresh the cache with the new tasks
                (newCities as? Result.Success)?.let { refreshCache(it.data) }

                cachedCities?.values?.let { tasks ->
                    return@withContext Result.Success(tasks.sortedBy { it.name })
                }

                (newCities as? Result.Success)?.let {
                    if (it.data.isEmpty()) {
                        return@withContext Result.Success(it.data)
                    }
                }

                return@withContext Result.Error(Exception("Illegal state"))
            }
        }
    }

    private suspend fun fetchCitiesFromRemoteOrLocal(forceUpdate: Boolean,idRegion: Int, idBudget: Int, idMunicipal: Int): Result<List<City>> {
        // Remote first
        val remoteCities = citiesRemoteDataSource.getCities(idRegion, idBudget, idMunicipal)
        when (remoteCities) {
            //is Error -> Timber.w("Remote data source fetch failed")
            is Result.Success -> {
                refreshLocalDataSource(remoteCities.data)
                return remoteCities
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localCities = citiesLocalDataSource.getCities(idRegion, idBudget, idMunicipal)
        if (localCities is Result.Success) return localCities
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(tasks: List<City>) {
        cachedCities?.clear()
        tasks.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(cities: List<City>) {
        //citiesLocalDataSource.deleteAllTasks()
        for (city in cities) {
            //citiesLocalDataSource.saveTask(city)
        }
    }

    private fun cacheCity(city: City): City {
        val cachedCity = City(city.id, city.name, city.saldoAmount, city.accrualAmount, city.paymentAmount)
        // Create if it doesn't exist.
        if (cachedCities == null) {
            cachedCities = ConcurrentHashMap()
        }
        cachedCities?.put(cachedCity.id, cachedCity)
        return cachedCity
    }

    private inline fun cacheAndPerform(task: City, perform: (City) -> Unit) {
        val cachedTask = cacheCity(task)
        perform(cachedTask)
    }
}