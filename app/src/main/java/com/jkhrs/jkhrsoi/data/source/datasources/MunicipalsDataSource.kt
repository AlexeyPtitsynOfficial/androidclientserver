package com.jkhrs.jkhrsoi.data.source.datasources

import com.jkhrs.jkhrsoi.data.Municipal
import com.jkhrs.jkhrsoi.data.Result

interface MunicipalsDataSource {

    suspend fun getMunicipals(idRegion: Int, idBudget: Int): Result<List<Municipal>>
}