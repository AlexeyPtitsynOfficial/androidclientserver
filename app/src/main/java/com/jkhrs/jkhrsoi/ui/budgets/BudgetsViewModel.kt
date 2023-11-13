package com.jkhrs.jkhrsoi.ui.budgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jkhrs.jkhrsoi.data.Budget
import com.jkhrs.jkhrsoi.data.Region
import com.jkhrs.jkhrsoi.data.Result
import com.jkhrs.jkhrsoi.data.source.repositories.interfaces.BudgetsRepository
import com.jkhrs.jkhrsoi.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

class BudgetsViewModel @Inject constructor(private val budgetsRepository: BudgetsRepository) : ViewModel() {

    private val _items = MutableLiveData<List<Budget>>().apply { value = emptyList() }
    val items: LiveData<List<Budget>> = _items

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val isDataLoadingError = MutableLiveData<Boolean>()

    fun loadBudgets(forceUpdate: Boolean, id_region: Int) {
        _dataLoading.value = true

        wrapEspressoIdlingResource {

            viewModelScope.launch {
                val budgetsResult = budgetsRepository.getBudgets(forceUpdate, id_region)

                if (budgetsResult is Result.Success) {
                    val budgets = budgetsResult.data

                    val budgetsToShow = ArrayList<Budget>()
                    for (budget in budgets){
                        budgetsToShow.add(budget)
                    }
                    // We filter the tasks based on the requestType
                    /*for (budget in budgets) {
                        when (_currentFiltering) {
                            TasksFilterType.ALL_TASKS -> tasksToShow.add(budget)
                            TasksFilterType.ACTIVE_TASKS -> if (budget.isActive) {
                                tasksToShow.add(budget)
                            }
                            TasksFilterType.COMPLETED_TASKS -> if (budget.isCompleted) {
                                tasksToShow.add(budget)
                            }
                        }
                    }*/
                    isDataLoadingError.value = false
                    _items.value = ArrayList(budgetsToShow)
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
