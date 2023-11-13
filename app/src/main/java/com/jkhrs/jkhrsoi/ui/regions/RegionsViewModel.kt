package com.jkhrs.jkhrsoi.ui.regions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkhrs.jkhrsoi.R
import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.RegionsRepository
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import org.acra.ACRA
import java.util.ArrayList
import javax.inject.Inject

class RegionsViewModel @Inject constructor(
    private val regionsRepository: RegionsRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Region>>().apply { value = emptyList() }
    val items: LiveData<List<Region>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val isDataLoadingError = MutableLiveData<Boolean>()

    fun loadRegions(forceUpdate: Boolean) {
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val regionsResult = regionsRepository.getRegions(forceUpdate)

                if (regionsResult is Result.Success) {
                    val regions = regionsResult.data

                    val regionsToShow = ArrayList<Region>()
                    for (region in regions){
                        regionsToShow.add(region)
                    }
                    // We filter the tasks based on the requestType
                    /*for (region in regions) {
                        when (_currentFiltering) {
                            TasksFilterType.ALL_TASKS -> tasksToShow.add(region)
                            TasksFilterType.ACTIVE_TASKS -> if (region.isActive) {
                                tasksToShow.add(region)
                            }
                            TasksFilterType.COMPLETED_TASKS -> if (region.isCompleted) {
                                tasksToShow.add(region)
                            }
                        }
                    }*/
                    isDataLoadingError.value = false
                    _items.value = ArrayList(regionsToShow)
                } else {
                    isDataLoadingError.value = false
                    _items.value = emptyList()
                    //ACRA.getErrorReporter().handleException(e);
                    //showSnackbarMessage(R.string.loading_error)
                }

                _dataLoading.value = false
            }
        }
    }
}