package com.jkhrs.jkhrsoi.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.Search
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.SearchRepository
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _items = MutableLiveData<List<Search>>().apply { value = emptyList() }
    val items: LiveData<List<Search>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val isDataLoadingError = MutableLiveData<Boolean>()

    fun loadSearchResult(forceUpdate: Boolean, searchText: String) {
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val searchResult = searchRepository.getSearchResult(forceUpdate, searchText)

                if (searchResult is Result.Success) {
                    val searchs = searchResult.data

                    val searchToShow = ArrayList<Search>()
                    for (search in searchs){
                        searchToShow.add(search)
                    }
                    // We filter the tasks based on the requestType
                    /*for (search in search) {
                        when (_currentFiltering) {
                            TasksFilterType.ALL_TASKS -> tasksToShow.add(search)
                            TasksFilterType.ACTIVE_TASKS -> if (search.isActive) {
                                tasksToShow.add(search)
                            }
                            TasksFilterType.COMPLETED_TASKS -> if (search.isCompleted) {
                                tasksToShow.add(search)
                            }
                        }
                    }*/
                    isDataLoadingError.value = false
                    _items.value = ArrayList(searchToShow)
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