package com.universitinder.app.filters.province

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.models.Filter
import com.universitinder.app.models.PROVINCES
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProvinceFilterViewModel(
    private val filterController: FilterController,
    val popActivity: () -> Unit,
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(ProvinceFilterUiState())
    val uiState : StateFlow<ProvinceFilterUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null) {
            _uiState.value = _uiState.value.copy(
                provinces = PROVINCES.toList(),
                loading = true
            )
            viewModelScope.launch(Dispatchers.IO) {
                val filter = filterController.getFilter(email = currentUser.email)
                if (filter != null) {
                    _uiState.value = _uiState.value.copy(
                        cities = filter.cities,
                        affordability = filter.affordability,
                        courses = filter.courses,
                        checkedProvinces = filter.provinces.split("___"),
                    )
                }
            }
            _uiState.value = _uiState.value.copy(loading = false)
        }
    }

    fun onCheckChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            checkedProvinces = if (_uiState.value.checkedProvinces.contains(newVal))
                _uiState.value.checkedProvinces.filter { it != newVal }
                else _uiState.value.checkedProvinces.plus(newVal)
        )
    }

    fun save() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = true)}
                val result = filterController.updateFilter(
                    email = currentUser.email,
                    filter = Filter(
                        provinces = _uiState.value.checkedProvinces.joinToString("___"),
                        cities = _uiState.value.cities,
                        affordability = _uiState.value.affordability,
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