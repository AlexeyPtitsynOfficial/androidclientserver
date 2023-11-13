package com.jkhrs.jkhrsoi.ui.cities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkhrs.jkhrsoi.data.Budget
import com.jkhrs.jkhrsoi.data.City
import com.jkhrs.jkhrsoi.data.Municipal
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.CitiesRepository
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class CitiesViewModel @Inject constructor(
    private val citiesRepository: CitiesRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<City>>().apply { value = emptyList() }
    val items: LiveData<List<City>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val isDataLoadingError = MutableLiveData<Boolean>()

    fun loadCities(forceUpdate: Boolean, idRegion: Int, idBudget: Int, idMunicipal: Int) {
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val citiesResult = citiesRepository.getCities(forceUpdate, idRegion, idBudget, idMunicipal)

                if (citiesResult is Result.Success) {
                    val cities = citiesResult.data

                    val citiesToShow = ArrayList<City>()
                    for (city in cities){
                        citiesToShow.add(city)
                    }
                    // We filter the tasks based on the requestType
                    /*for (city in cities) {
                        when (_currentFiltering) {
                            TasksFilterType.ALL_TASKS -> tasksToShow.add(city)
                            TasksFilterType.ACTIVE_TASKS -> if (city.isActive) {
                                tasksToShow.add(city)
                            }
                            TasksFilterType.COMPLETED_TASKS -> if (city.isCompleted) {
                                tasksToShow.add(city)
                            }
                        }
                    }*/
                    isDataLoadingError.value = false
                    _items.value = ArrayList(citiesToShow)
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
