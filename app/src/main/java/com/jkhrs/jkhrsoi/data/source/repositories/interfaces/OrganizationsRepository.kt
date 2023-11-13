package com.jkhrs.jkhrsoi.data.source.repositories.interfaces

import com.jkhrs.jkhrsoi.data.Organization
import com.jkhrs.jkhrsoi.data.Result

interface OrganizationsRepository {

    suspend fun getOrganizations(forceUpdate: Boolean = false, idRegion : Int, idBudget: Int): Result<List<Organization>>
}