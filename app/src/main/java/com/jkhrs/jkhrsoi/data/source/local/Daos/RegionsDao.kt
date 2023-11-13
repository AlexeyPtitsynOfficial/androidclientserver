package com.jkhrs.jkhrsoi.data.source.local.Daos

import androidx.room.Dao
import androidx.room.Query
import com.jkhrs.jkhrsoi.data.Region

@Dao
interface RegionsDao {

    @Query("SELECT * FROM Regions")
    suspend fun getRegions(): List<Region>
}