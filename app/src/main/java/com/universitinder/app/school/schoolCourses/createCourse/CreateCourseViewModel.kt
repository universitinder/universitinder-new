package com.universitinder.app.school.schoolCourses.createCourse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.models.COURSE_DURATION_STRING_TO_INT_MAP
import com.universitinder.app.models.Course
import com.universitinder.app.models.EducationLevel
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.School
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateCourseViewModel(
    private val school: School,
    private val courseController: CourseController,
    val popActivity: () -> Unit,
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(CreateCourseUiState())
    val uiState : StateFlow<CreateCourseUiState> = _uiState.asStateFlow()

    fun onNameChange(newVal: String) { _uiState.value = _uiState.value.copy(name = newVal) }
    fun onDurationChange(newVal: String) { _uiState.value = _uiState.value.copy(duration = newVal) }
    fun onDurationMenuToggle() { _uiState.value = _uiState.value.copy(durationMenuExpanded = !_uiState.value.durationMenuExpanded) }
    fun onLevelChange(newVal: String) { _uiState.value = _uiState.value.copy(level = EducationLevel.valueOf(newVal)) }
    fun onLevelMenuToggle() { _uiState.value = _uiState.value.copy(levelMenuExpanded = !_uiState.value.levelMenuExpanded) }

    fun fieldsNotFilled() : Boolean {
        val state = _uiState.value
        return state.name.isEmpty() || state.name.isBlank() || state.duration.isEmpty() || state.duration.isBlank()
    }

    fun createCourse() {
        if (fieldsNotFilled()) {
            _uiState.value = _uiState.value.copy(
                resultMessage = ResultMessage(
                    show = true,
                    type = ResultMessageType.FAILED,
                    message = "Please fill in all the fields"
                )
            )
            return
        }

        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(createLoading = true) }
                val result = courseController.createCourse(
                    documentID = school.documentID,
                    course = Course(
                        name = _uiState.value.name,
                        duration = COURSE_DURATION_STRING_TO_INT_MAP[_uiState.value.duration] ?: 0,
                        level = _uiState.value.level
                    )
                )
                if (result) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully created course"
                            ),
                            createLoading = false
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.FAILED,
                                message = "Course creation unsuccessful"
                            ),
                            createLoading = false
                        )
                    }
                }

            }
        }
    }

}