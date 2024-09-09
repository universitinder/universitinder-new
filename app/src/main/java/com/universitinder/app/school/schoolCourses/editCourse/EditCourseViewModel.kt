package com.universitinder.app.school.schoolCourses.editCourse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.models.Course
import com.universitinder.app.models.EducationLevel
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditCourseViewModel(
    private val documentID: String,
    private val courseController: CourseController,
    val popActivity: () -> Unit
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(EditCourseUiState())
    val uiState : StateFlow<EditCourseUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val course = courseController.getCourse(email = currentUser.email, documentID = documentID)
                if (course != null) {
                    onNameChange(course.name)
                    onDurationChange(course.duration.toString())
                    onLevelChange(course.level.toString())
                }
            }
        }
    }

    fun onNameChange(newVal: String) { _uiState.value = _uiState.value.copy(name = newVal) }
    fun onDurationChange(newVal: String) { _uiState.value = _uiState.value.copy(duration = newVal) }
    fun onLevelChange(newVal: String) { _uiState.value = _uiState.value.copy(level = EducationLevel.valueOf(newVal), levelMenuExpanded = false) }
    fun onLevelMenuToggle() { _uiState.value = _uiState.value.copy(levelMenuExpanded = !_uiState.value.levelMenuExpanded) }
    fun onDeleteDialogToggle() { _uiState.value = _uiState.value.copy(showDeleteDialog = !_uiState.value.showDeleteDialog) }

    fun fieldsNotFilled() : Boolean {
        val state = _uiState.value
        return state.name.isEmpty() || state.name.isBlank() || state.duration.isEmpty() || state.duration.isBlank()
    }

    fun updateCourse() {
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
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(updateLoading = true) }
                val result = courseController.updateCourse(email = currentUser.email, documentID = documentID, course = Course(name = _uiState.value.name, duration = _uiState.value.duration.toInt(), level = _uiState.value.level))
                if (result) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully updated course data"
                            ),
                            updateLoading = false
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.FAILED,
                                message = "Course information update unsuccessful"
                            ),
                            updateLoading = false
                        )
                    }
                }

            }
        }
    }

    fun deleteCourse() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(deleteLoading = true) }
                val result = courseController.deleteCourse(email = currentUser.email, documentID = documentID)
                if (result) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            deleteResultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully deleted course"
                            ),
                            deleteLoading = false
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            deleteResultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.FAILED,
                                message = "Course deletion unsuccessful"
                            ),
                            deleteLoading = false
                        )
                    }
                }
            }
        }
    }
}