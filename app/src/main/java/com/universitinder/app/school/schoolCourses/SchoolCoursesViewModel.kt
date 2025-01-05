package com.universitinder.app.school.schoolCourses

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.universitinder.app.components.CourseDurationMap
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.COURSE_DURATION_INT_TO_STRING_MAP
import com.universitinder.app.models.CourseDurations
import com.universitinder.app.models.School
import com.universitinder.app.models.UserState
import com.universitinder.app.school.schoolCourses.createCourse.CreateCourseActivity
import com.universitinder.app.school.schoolCourses.editCourse.EditCourseActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolCoursesViewModel(
    private val school: School,
    private val courseController: CourseController,
    private val schoolController: SchoolController,
    private val activityStarterHelper: ActivityStarterHelper,
    val popActivity: () -> Unit
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(SchoolCoursesUiState())
    val uiState : StateFlow<SchoolCoursesUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun onTabChange(newTab: Int) { _uiState.value = _uiState.value.copy(selectedTab = newTab) }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val courses = courseController.getCourses(documentID = school.documentID)
                val durations = schoolController.getSchoolDurations(documentID = school.documentID)
                val courseDurationMapList = createCourseDurationMapList(durations)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        fetchingLoading = false,
                        courseDurationMapList = courseDurationMapList
                    )
                    onCoursesChange(courses)
                }
            }
        }
    }

    private fun onCourseDurationClicked(title: String) {
        _uiState.value = _uiState.value.copy(
            courseDurationMapList = _uiState.value.courseDurationMapList.map {
                if (it.title == title) {
                    viewModelScope.launch(Dispatchers.IO) {
                        if (currentUser != null) {
                            if (title == "2 YEARS") {
                                schoolController.updateSchool2YearCourse(school.documentID, !it.clicked)
                            }
                            if (title == "3 YEARS") {
                                schoolController.updateSchool3YearCourse(school.documentID, !it.clicked)
                            }
                            if (title == "4 YEARS") {
                                schoolController.updateSchool4YearCourse(school.documentID, !it.clicked)
                            }
                            if (title == "5 YEARS") {
                                schoolController.updateSchool5YearCourse(school.documentID, !it.clicked)
                            }
                        }
                    }
                    return@map CourseDurationMap(it.title, !it.clicked, it.onChange)
                }
                it
            }
        )
    }

    private fun createCourseDurationMapList(courseDurations: CourseDurations?) : List<CourseDurationMap> {
        if (courseDurations == null) return emptyList()
        return listOf(
            CourseDurationMap(COURSE_DURATION_INT_TO_STRING_MAP[2]!!, clicked = courseDurations.has2YearCourse, onChange = { onCourseDurationClicked(
                COURSE_DURATION_INT_TO_STRING_MAP[2]!!) }),
            CourseDurationMap(COURSE_DURATION_INT_TO_STRING_MAP[3]!!, clicked = courseDurations.has3YearCourse, onChange = { onCourseDurationClicked(
                COURSE_DURATION_INT_TO_STRING_MAP[3]!!) }),
            CourseDurationMap(COURSE_DURATION_INT_TO_STRING_MAP[4]!!, clicked = courseDurations.has4YearCourse, onChange = { onCourseDurationClicked(
                COURSE_DURATION_INT_TO_STRING_MAP[4]!!) }),
            CourseDurationMap(COURSE_DURATION_INT_TO_STRING_MAP[5]!!, clicked = courseDurations.has5YearCourse, onChange = { onCourseDurationClicked(
                COURSE_DURATION_INT_TO_STRING_MAP[5]!!) })
        )
    }

    private fun onCoursesChange(courses: List<DocumentSnapshot>) { _uiState.value = _uiState.value.copy(courses = courses) }

    fun startCreateCourseActivity() {
        val intent = Intent(activityStarterHelper.getContext(), CreateCourseActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startEditCourseActivity(documentID: String) {
        val intent = Intent(activityStarterHelper.getContext(), EditCourseActivity::class.java)
        intent.putExtra("documentID", documentID)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }
}