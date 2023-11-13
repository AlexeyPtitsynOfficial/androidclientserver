package com.jkhrs.jkhrsoi.data.source.repositories.interfaces

import com.jkhrs.jkhrsoi.data.Municipal
import com.jkhrs.jkhrsoi.data.Result

interface MunicipalsRepository {

    suspend fun getMunicipals(forceUpdate: Boolean = false, idRegion: Int, idBudget: Int): Result<List<Municipal>>
}