package com.jkhrs.jkhrsoi.data.source.local.Daos

import androidx.room.Dao
import androidx.room.Query
import com.jkhrs.jkhrsoi.data.Municipal

@Dao
interface MunicipalsDao {

    @Query("SELECT * FROM Municipals")
    suspend fun getMunicipals(): List<Municipal>
}