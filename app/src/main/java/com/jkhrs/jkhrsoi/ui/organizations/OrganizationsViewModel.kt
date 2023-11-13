package com.jkhrs.jkhrsoi.ui.organizations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkhrs.jkhrsoi.data.Organization
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.OrganizationsRepository
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class OrganizationsViewModel @Inject constructor(private val organizationsRepository: OrganizationsRepository) : ViewModel() {

    private val _items = MutableLiveData<List<Organization>>().apply { value = emptyList() }
    val items: LiveData<List<Organization>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val isDataLoadingError = MutableLiveData<Boolean>()

    fun loadOrganizations(forceUpdate: Boolean, idRegion: Int, idBudget: Int, SearchText: String) {
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val organizationsResult = organizationsRepository.getOrganizations(forceUpdate, idRegion, idBudget)

                if (organizationsResult is Result.Success) {
                    val organizations = organizationsResult.data

                    val organizationsToShow = ArrayList<Organization>()
                    for (organization in organizations){
                        organizationsToShow.add(organization)
                    }
                    // We filter the tasks based on the requestType
                    /*for (organization in organizations) {
                        when (_currentFiltering) {
                            TasksFilterType.ALL_TASKS -> tasksToShow.add(organization)
                            TasksFilterType.ACTIVE_TASKS -> if (organization.isActive) {
                                tasksToShow.add(organization)
                            }
                            TasksFilterType.COMPLETED_TASKS -> if (organization.isCompleted) {
                                tasksToShow.add(organization)
                            }
                        }
                    }*/
                    isDataLoadingError.value = false
                    _items.value = ArrayList(organizationsToShow)
                } else {
                    isDataLoadingError.value = false
                    _items.value = emptyList()
                    //showSnackbarMessage(R.string.loading_tasks_error)
                }

                _dataLoading.value = false
            }
        }
    }
}
