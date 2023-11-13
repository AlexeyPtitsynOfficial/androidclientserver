package com.jkhrs.jkhrsoi.ui.municipals

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkhrs.jkhrsoi.data.Municipal
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.MunicipalsRepository
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class MunicipalsViewModel @Inject constructor(
    private val municipalsRepository: MunicipalsRepository
) : ViewModel()  {
    private val _items = MutableLiveData<List<Municipal>>().apply { value = emptyList() }
    val items: LiveData<List<Municipal>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val isDataLoadingError = MutableLiveData<Boolean>()

    init {
        //(true)
    }

    fun loadMunicipals(forceUpdate: Boolean, idRegion: Int, idBudget: Int) {
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val municipalsResult = municipalsRepository.getMunicipals(forceUpdate, idRegion, idBudget)

                if (municipalsResult is Result.Success) {
                    val municipals = municipalsResult.data

                    val municipalsToShow = ArrayList<Municipal>()
                    for (municipal in municipals){
                        municipalsToShow.add(municipal)
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
                    _items.value = ArrayList(municipalsToShow)
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
