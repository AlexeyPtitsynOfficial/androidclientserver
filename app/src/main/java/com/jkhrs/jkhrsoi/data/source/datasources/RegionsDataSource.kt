package com.jkhrs.jkhrsoi.data.source.datasources

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result

interface RegionsDataSource {

    suspend fun getRegions(): Result<List<Region>>
}