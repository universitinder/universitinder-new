package com.universitinder.app.filters.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.CourseController
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

class CoursesFilterViewModel(
    private val filterController: FilterController,
    private val courseController: CourseController,
    val popActivity: () -> Unit,
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(CoursesFilterUiState())
    val uiState : StateFlow<CoursesFilterUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null) {
            _uiState.value = _uiState.value.copy(loading = true)
            viewModelScope.launch(Dispatchers.IO) {
                val courses = courseController.getAllUniqueCourses()
                val filter = filterController.getFilter(email = currentUser.email)
                if (filter != null) {
                    _uiState.value = _uiState.value.copy(
                        provinces = filter.provinces,
                        cities = filter.cities,
                        affordability = filter.affordability,
                        courses = courses.map { course -> course.name },
                        checkedCourses = filter.courses.split("___"),
                    )
                }
            }
            _uiState.value = _uiState.value.copy(loading = false)
        }
    }

    fun onCheckChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            checkedCourses = if (_uiState.value.checkedCourses.contains(newVal))
                _uiState.value.checkedCourses.filter { it != newVal }
            else _uiState.value.checkedCourses.plus(newVal)
        )
    }

    fun save() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = true)}
                val result = filterController.updateFilter(
                    email = currentUser.email,
                    filter = Filter(
                        provinces = _uiState.value.provinces,
                        cities = _uiState.value.cities,
                        affordability = _uiState.value.affordability,
                        courses = _uiState.value.checkedCourses.joinToString("___")
                    )
                )
                if (result) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully saved courses filter"
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
                                message = "Saving courses filter unsuccessful"
                            )
                        )
                    }
                }
            }
        }
    }
}