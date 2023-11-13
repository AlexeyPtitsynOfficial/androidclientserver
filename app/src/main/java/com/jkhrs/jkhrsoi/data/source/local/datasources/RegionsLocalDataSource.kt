package com.jkhrs.jkhrsoi.data.source.local.datasources

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.source.datasources.RegionsDataSource
import com.jkhrs.jkhrsoi.data.source.local.Daos.RegionsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RegionsLocalDataSource internal constructor(
    private val regionsDao: RegionsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : RegionsDataSource {

    override suspend fun getRegions(): Result<List<Region>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(regionsDao.getRegions())
        } catch (e: Exception) {
            Error(e)
        }
    }
}