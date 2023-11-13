package com.jkhrs.jkhrsoi.data.source.local.datasources

import com.jkhrs.jkhrsoi.data.Organization
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.datasources.OrganizationsDataSource
import com.jkhrs.jkhrsoi.data.source.local.Daos.OrganizationsDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OrganizationsLocalDataSource internal constructor(
    private val organizationsDao: OrganizationsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): OrganizationsDataSource {
    override suspend fun getOrganizations(idRegion: Int, idBudget: Int): Result<List<Organization>> = withContext(ioDispatcher){
        return@withContext try {
            Result.Success(organizationsDao.getOrganizations())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}