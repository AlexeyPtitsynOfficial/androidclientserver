package com.jkhrs.jkhrsoi.data.source.datasources

import com.jkhrs.jkhrsoi.data.City
import com.jkhrs.jkhrsoi.data.Result

interface CitiesDataSource {
    suspend fun getCities(idRegion: Int, idBudget: Int, idMunicipal: Int): Result<List<City>>
}