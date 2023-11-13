package com.jkhrs.jkhrsoi.data.source.local.Daos

import androidx.room.Dao
import androidx.room.Query
import com.jkhrs.jkhrsoi.data.City

@Dao
interface CitiesDao {
    @Query("SELECT * FROM cities")
    suspend fun getCities():List<City>
}