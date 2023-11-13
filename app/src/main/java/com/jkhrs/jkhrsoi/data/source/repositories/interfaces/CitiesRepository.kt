package com.jkhrs.jkhrsoi.data.source.repositories.interfaces

import com.jkhrs.jkhrsoi.data.City
import com.jkhrs.jkhrsoi.data.Result

interface CitiesRepository {
    suspend fun getCities(forceUpdate: Boolean = false,idRegion: Int, idBudget: Int, idMunicipal: Int): Result<List<City>>
}