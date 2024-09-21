package com.universitinder.app.filters.affordability

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.models.Filter
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AffordabilityFilterViewModel(
    private val filterController: FilterController,
    val popActivity: () -> Unit
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(AffordabilityUiState())
    val uiState : StateFlow<AffordabilityUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null) {
            _uiState.value = _uiState.value.copy(loading = true)
            viewModelScope.launch(Dispatchers.IO) {
                val filter = filterController.getFilter(email = currentUser.email)
                if (filter != null) {
                    _uiState.value = _uiState.value.copy(
                        cities = filter.cities,
                        provinces = filter.provinces,
                        affordability = filter.affordability,
                        minimum = filter.minimum,
                        maximum = filter.maximum,
                        courses = filter.courses
                    )
                }
            }
            _uiState.value = _uiState.value.copy(loading = false)
        }
    }

    fun onMinimumChange(newVal: String) { _uiState.value = _uiState.value.copy(minimum = if (newVal.isEmpty() || newVal.isBlank()) 0 else newVal.toInt()) }
    fun onMaximumChange(newVal: String) {
        _uiState.value = _uiState.value.copy(maximum = if (newVal.isEmpty() || newVal.isBlank()) 0 else newVal.toInt())
        determineAffordability(_uiState.value.maximum)
    }
    private fun onAffordabilityChange(newVal: Int) { _uiState.value = _uiState.value.copy(affordability = newVal) }

    private fun determineAffordability(maxVal: Int) {
        if (maxVal <= 10000) onAffordabilityChange(1)
        else if (maxVal <= 50000) onAffordabilityChange(2)
        else onAffordabilityChange(3)
    }

    fun save() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = true)}
                val result = filterController.updateFilter(
                    email = currentUser.email,
                    filter = Filter(
                        cities = _uiState.value.cities,
                        provinces = _uiState.value.provinces,
                        affordability = _uiState.value.affordability,
                        minimum = _uiState.value.minimum,
                        maximum = _uiState.value.maximum,
                        courses = _uiState.value.courses
                    )
                )
                if (result) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully saved province filter"
                            )
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.FAILED,
                                message = "Saving province filter unsuccessful"
                            )
                        )
                    }
                }
            }
        }
    }
}