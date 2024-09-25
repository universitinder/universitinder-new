package com.universitinder.app.school.schoolCourses

import com.google.firebase.firestore.DocumentSnapshot

data class SchoolCoursesUiState (
    val fetchingLoading: Boolean = false,
    val courses: List<DocumentSnapshot> = listOf(),

)
