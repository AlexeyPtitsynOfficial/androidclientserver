package com.jkhrs.jkhrsoi.data.source.datasources

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Search

interface SearchDataSource {

    suspend fun getSearchResult(searchText: String): Result<List<Search>>
}