package com.jkhrs.jkhrsoi.data.source.local.Daos

import androidx.room.Dao
import androidx.room.Query
import com.jkhrs.jkhrsoi.data.Organization

@Dao
interface OrganizationsDao {
    @Query("SELECT * FROM organizations")
    suspend fun getOrganizations():List<Organization>
}