package com.universitinder.app.school.schoolCourses

import com.google.firebase.firestore.DocumentSnapshot
import com.universitinder.app.components.CourseDurationMap

data class SchoolCoursesUiState (
    val fetchingLoading: Boolean = false,
    val courses: List<DocumentSnapshot> = listOf(),
    val selectedTab: Int = 0,
    val courseDurationMapList: List<CourseDurationMap> = listOf()
)
