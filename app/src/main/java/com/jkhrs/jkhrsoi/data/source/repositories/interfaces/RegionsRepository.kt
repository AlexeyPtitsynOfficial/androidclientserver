package com.jkhrs.jkhrsoi.data.source.repositories.interfaces

import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result

interface RegionsRepository {

    suspend fun getRegions(forceUpdate: Boolean = false): Result<List<Region>>
}