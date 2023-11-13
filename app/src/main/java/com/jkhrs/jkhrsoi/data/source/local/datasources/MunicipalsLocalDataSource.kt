package com.jkhrs.jkhrsoi.data.source.local.datasources

import com.jkhrs.jkhrsoi.data.Municipal
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.source.datasources.MunicipalsDataSource
import com.jkhrs.jkhrsoi.data.source.local.Daos.MunicipalsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MunicipalsLocalDataSource internal constructor(
    private val municipalsDao: MunicipalsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : MunicipalsDataSource {
    override suspend fun getMunicipals(idRegion: Int, idBudget: Int): Result<List<Municipal>> = withContext(ioDispatcher){
        return@withContext try {
            Success(municipalsDao.getMunicipals())
        } catch (e: Exception) {
            Error(e)
        }
    }
}