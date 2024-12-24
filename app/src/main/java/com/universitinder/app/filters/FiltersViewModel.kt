package com.universitinder.app.filters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.models.Filter
import com.universitinder.app.models.MUNICIPALITIES_AND_CITIES
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

class FiltersViewModel(
    private val courseController: CourseController,
    private val filterController: FilterController,
    val popActivity: () -> Unit,
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(FiltersUiState())
    val uiState: StateFlow<FiltersUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null) {
            _uiState.value = _uiState.value.copy(
                provinces = PROVINCES.toList(),
                loading = true
            )
            viewModelScope.launch(Dispatchers.IO) {
                val courses = courseController.getAllUniqueCourses()
                val filter = filterController.getFilter(email = currentUser.email)
                if (filter != null) {
                    _uiState.value = _uiState.value.copy(
                        cities = MUNICIPALITIES_AND_CITIES["Pampanga"]?.toList()!!,
                        checkedCities = if (filter.cities == "") emptyList() else filter.cities.split("___"),
                        affordability = filter.affordability,
                        minimum = filter.minimum.toString(), // Convert to String
                        maximum = filter.maximum.toString(), // Convert to String
                        courses = courses.map { course -> course.name },
                        checkedCourses = if (filter.courses == "") emptyList() else filter.courses.split("___"),
                        checkedProvinces = if (filter.provinces == "") emptyList() else  filter.provinces.split("___"),
                        checkedPrivatePublic = listOf(if (filter.private) "PRIVATE" else "", if (filter.public) "PUBLIC" else ""),
                        checkedDurations = listOf(if (filter.has2YearCourse) "2 YEARS" else "", if (filter.has3YearCourse) "3 YEARS" else "", if (filter.has4YearCourse) "4 YEARS" else "", if (filter.has5YearCourse) "5 YEARS" else "")
                    )
                }
            }
            _uiState.value = _uiState.value.copy(loading = false)
        }
    }

    fun onMinimumChange(newVal: String) {
        _uiState.value = _uiState.value.copy(minimum = newVal)
    }

    fun onMaximumChange(newVal: String) {
        _uiState.value = _uiState.value.copy(maximum = newVal)
    }

    private fun onAffordabilityChange(newVal: Int) {
        _uiState.value = _uiState.value.copy(affordability = newVal)
    }

    private fun determineAffordability(maxVal: Int) {
        if (maxVal <= 10000) onAffordabilityChange(1)
        else if (maxVal <= 50000) onAffordabilityChange(2)
        else onAffordabilityChange(3)
    }

    fun onCityCheckChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            checkedCities = if (_uiState.value.checkedCities.contains(newVal))
                _uiState.value.checkedCities.filter { it != newVal }
            else _uiState.value.checkedCities.plus(newVal)
        )
    }

    fun onCoursesCheckChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            checkedCourses = if (_uiState.value.checkedCourses.contains(newVal))
                _uiState.value.checkedCourses.filter { it != newVal }
            else _uiState.value.checkedCourses.plus(newVal)
        )
    }

    fun onProvinceCheckChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            checkedProvinces = if (_uiState.value.checkedProvinces.contains(newVal))
                _uiState.value.checkedProvinces.filter { it != newVal }
            else _uiState.value.checkedProvinces.plus(newVal)
        )
    }

    fun onPrivatePublicCheckChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            checkedPrivatePublic = if (_uiState.value.checkedPrivatePublic.contains(newVal))
                _uiState.value.checkedPrivatePublic.filter { it != newVal }
            else _uiState.value.checkedPrivatePublic.plus(newVal)
        )
    }

    fun onDurationCheckChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            checkedDurations = if (_uiState.value.checkedDurations.contains(newVal))
                _uiState.value.checkedDurations.filter { it != newVal }
            else _uiState.value.checkedDurations.plus(newVal)
        )
    }

    fun clear() {
        _uiState.value = _uiState.value.copy(
            checkedDurations = emptyList(),
            checkedPrivatePublic = emptyList(),
            checkedCourses = emptyList(),
            checkedCities = emptyList(),
            checkedProvinces = emptyList(),
            minimum = "",
            maximum = "",
            affordability = 0
        )
    }

    fun save() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(saveLoading = true) }
                val result = filterController.updateFilter(
                    email = currentUser.email,
                    filter = Filter(
                        provinces = _uiState.value.checkedProvinces.joinToString("___"),
                        cities = _uiState.value.checkedCities.joinToString("___"),
                        affordability = _uiState.value.affordability,
                        minimum = _uiState.value.minimum.toIntOrNull() ?: 0, // Convert to Int with default value
                        maximum = _uiState.value.maximum.toIntOrNull() ?: 0, // Convert to Int with default value
                        courses = _uiState.value.checkedCourses.joinToString("___"),
                        public = _uiState.value.checkedPrivatePublic.contains("PUBLIC"),
                        private = _uiState.value.checkedPrivatePublic.contains("PRIVATE"),
                        has2YearCourse = _uiState.value.checkedDurations.contains("2 YEARS"),
                        has3YearCourse = _uiState.value.checkedDurations.contains("3 YEARS"),
                        has4YearCourse = _uiState.value.checkedDurations.contains("4 YEARS"),
                        has5YearCourse = _uiState.value.checkedDurations.contains("5 YEARS"),
                        courseDuration = _uiState.value.checkedDurations.joinToString("___")
                    )
                )
                if (result) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            saveLoading = false,
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully saved filters"
                            )
                        )
                    }
                    popActivity()
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            saveLoading = false,
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.FAILED,
                                message = "Saving filters unsuccessful"
                            )
                        )
                    }
                }
            }
        }
    }
}