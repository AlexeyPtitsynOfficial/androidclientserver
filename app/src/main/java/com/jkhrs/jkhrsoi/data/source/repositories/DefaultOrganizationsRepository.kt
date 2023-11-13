package com.jkhrs.jkhrsoi.data.source.repositories

import com.jkhrs.jkhrsoi.data.Organization
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.datasources.OrganizationsDataSource
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.OrganizationsRepository
import com.jkhrs.jkhrsoi.di.ApplicationModule
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.inject.Inject

class DefaultOrganizationsRepository @Inject constructor(
    @ApplicationModule.OrganizationsRemoteDataSource private val organizationsRemoteDataSource: OrganizationsDataSource,
    @ApplicationModule.OrganizationsLocalDataSource private val organizationsLocalDataSource: OrganizationsDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : OrganizationsRepository {

    private var cachedOrganizations: ConcurrentMap<Long, Organization>? = null

    override suspend fun getOrganizations(forceUpdate: Boolean, idRegion: Int, idBudget: Int): Result<List<Organization>> {
        wrapEspressoIdlingResource {
            return withContext(ioDispatcher) {
                // Respond immediately with cache if available and not dirty
                if (!forceUpdate) {
                    cachedOrganizations?.let { cachedTasks ->
                        return@withContext Result.Success(cachedTasks.values.sortedBy { it.name })
                    }
                }

                val newOrganizations = fetchOrganizationsFromRemoteOrLocal(forceUpdate, idRegion, idBudget)

                // Refresh the cache with the new tasks
                (newOrganizations as? Result.Success)?.let { refreshCache(it.data) }

                cachedOrganizations?.values?.let { tasks ->
                    return@withContext Result.Success(tasks.sortedBy { it.name })
                }

                (newOrganizations as? Result.Success)?.let {
                    if (it.data.isEmpty()) {
                        return@withContext Result.Success(it.data)
                    }
                }

                return@withContext Result.Error(Exception("Illegal state"))
            }
        }
    }

    private suspend fun fetchOrganizationsFromRemoteOrLocal(forceUpdate: Boolean, idRegion: Int, idBudget: Int): Result<List<Organization>> {
        // Remote first
        val remoteOrganizations = organizationsRemoteDataSource.getOrganizations(idRegion, idBudget)
        when (remoteOrganizations) {
            //is Error -> Timber.w("Remote data source fetch failed")
            is Result.Success -> {
                refreshLocalDataSource(remoteOrganizations.data)
                return remoteOrganizations
            }
            else -> throw IllegalStateException()
        }

        // Don't read from local if it's forced
        if (forceUpdate) {
            return Result.Error(Exception("Can't force refresh: remote data source is unavailable"))
        }

        // Local if remote fails
        val localOrganizations = organizationsLocalDataSource.getOrganizations(idRegion, idBudget)
        if (localOrganizations is Result.Success) return localOrganizations
        return Result.Error(Exception("Error fetching from remote and local"))
    }

    private fun refreshCache(tasks: List<Organization>) {
        cachedOrganizations?.clear()
        tasks.sortedBy { it.id }.forEach {
            cacheAndPerform(it) {}
        }
    }

    private suspend fun refreshLocalDataSource(organizations: List<Organization>) {
        //organizationsLocalDataSource.deleteAllTasks()
        for (organization in organizations) {
            //organizationsLocalDataSource.saveTask(organization)
        }
    }

    private fun cacheOrganization(organization: Organization): Organization {
        val cachedOrganization = Organization(organization.id, organization.name, organization.saldoAmount, organization.accrualAmount, organization.paymentAmount)
        // Create if it doesn't exist.
        if (cachedOrganizations == null) {
            cachedOrganizations = ConcurrentHashMap()
        }
        cachedOrganizations?.put(cachedOrganization.id, cachedOrganization)
        return cachedOrganization
    }

    private inline fun cacheAndPerform(task: Organization, perform: (Organization) -> Unit) {
        val cachedTask = cacheOrganization(task)
        perform(cachedTask)
    }
}