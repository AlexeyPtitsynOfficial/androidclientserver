package com.jkhrs.jkhrsoi.data.source.repositories.interfaces

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Search

interface SearchRepository {

    suspend fun getSearchResult(forceUpdate: Boolean = false, searchText: String): Result<List<Search>>
}