package com.jkhrs.jkhrsoi.data.source.local.Daos

import androidx.room.Dao
import androidx.room.Query
import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Search

@Dao
interface SearchDao {

    @Query("SELECT * FROM Search")
    suspend fun getSearchResult(): List<Search>
}