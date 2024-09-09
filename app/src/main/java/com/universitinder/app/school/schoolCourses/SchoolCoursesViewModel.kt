package com.universitinder.app.school.schoolCourses

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.models.UserState
import com.universitinder.app.school.schoolCourses.createCourse.CreateCourseActivity
import com.universitinder.app.school.schoolCourses.editCourse.EditCourseActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolCoursesViewModel(
    private val courseController: CourseController,
    private val activityStarterHelper: ActivityStarterHelper,
    val popActivity: () -> Unit
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(SchoolCoursesUiState())
    val uiState : StateFlow<SchoolCoursesUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val courses = courseController.getCourses(email = currentUser.email)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(fetchingLoading = false)
                    onCoursesChange(courses)
                }
            }
        }
    }

    private fun onCoursesChange(courses: List<DocumentSnapshot>) { _uiState.value = _uiState.value.copy(courses = courses) }

    fun startCreateCourseActivity() {
        val intent = Intent(activityStarterHelper.getContext(), CreateCourseActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startEditCourseActivity(documentID: String) {
        val intent = Intent(activityStarterHelper.getContext(), EditCourseActivity::class.java)
        intent.putExtra("documentID", documentID)
        activityStarterHelper.startActivity(intent)
    }
}