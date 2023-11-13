package com.jkhrs.jkhrsoi.data.source.datasources

import com.jkhrs.jkhrsoi.data.Organization
import com.jkhrs.jkhrsoi.data.Result

interface OrganizationsDataSource {
    suspend fun getOrganizations(idRegion: Int, idBudget: Int): Result<List<Organization>>
}