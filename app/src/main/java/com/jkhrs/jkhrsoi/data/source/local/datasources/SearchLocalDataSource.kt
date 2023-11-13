package com.jkhrs.jkhrsoi.data.source.local.datasources

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Result.Success
import com.jkhrs.jkhrsoi.data.Result.Error
import com.jkhrs.jkhrsoi.data.Search
import com.jkhrs.jkhrsoi.data.source.datasources.RegionsDataSource
import com.jkhrs.jkhrsoi.data.source.datasources.SearchDataSource
import com.jkhrs.jkhrsoi.data.source.local.Daos.RegionsDao
import com.jkhrs.jkhrsoi.data.source.local.Daos.SearchDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchLocalDataSource internal constructor(
    private val searchDao: SearchDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SearchDataSource {

    override suspend fun getSearchResult(searchText: String): Result<List<Search>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(searchDao.getSearchResult())
        } catch (e: Exception) {
            Error(e)
        }
    }
}