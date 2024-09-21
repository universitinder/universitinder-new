package com.universitinder.app.filters.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.models.Filter
import com.universitinder.app.models.MUNICIPALITIES_AND_CITIES
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CityFilterViewModel(
    private val filterController: FilterController,
    val popActivity: () -> Unit
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(CityFilterUiState())
    val uiState : StateFlow<CityFilterUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null) {
            _uiState.value = _uiState.value.copy(loading = true)
            viewModelScope.launch(Dispatchers.IO) {
                val filter = filterController.getFilter(email = currentUser.email)
                if (filter != null) {
                    val cities : List<List<String>> = filter.provinces.split("___").map { MUNICIPALITIES_AND_CITIES[it]?.toList() ?: emptyList() }
                    _uiState.value = _uiState.value.copy(
                        cities = cities.flatten(),
                        checkedCities = filter.cities.split("___"),
                        provinces = filter.provinces,
                        affordability = filter.affordability,
                        minimum = filter.minimum,
                        maximum = filter.maximum,
                        courses = filter.courses,
                    )
                }
            }
            _uiState.value = _uiState.value.copy(loading = false)
        }
    }

    fun onCheckChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            checkedCities = if (_uiState.value.checkedCities.contains(newVal))
                _uiState.value.checkedCities.filter { it != newVal }
            else _uiState.value.checkedCities.plus(newVal)
        )
    }

    fun save() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = true)}
                val result = filterController.updateFilter(
                    email = currentUser.email,
                    filter = Filter(
                        cities = _uiState.value.checkedCities.joinToString("___"),
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