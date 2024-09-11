package com.universitinder.app.filters.courses

import com.universitinder.app.models.ResultMessage

data class CoursesFilterUiState(
    val provinces: String = "",
    val cities: String = "",
    val affordability: Int = 0,
    val courses: List<String> = listOf(),
    val checkedCourses: List<String> = listOf(),
    val loading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage(),
)
