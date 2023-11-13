package com.jkhrs.jkhrsoi.data.source.local.datasources

import com.jkhrs.jkhrsoi.data.City
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.datasources.CitiesDataSource
import com.jkhrs.jkhrsoi.data.source.local.Daos.CitiesDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CitiesLocalDataSource internal constructor(
    private val citiesDao: CitiesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): CitiesDataSource{
    override suspend fun getCities(idRegion: Int, idBudget: Int, idMunicipal : Int): Result<List<City>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(citiesDao.getCities())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}