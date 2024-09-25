package com.universitinder.app.school.schoolCourses.createCourse

import com.universitinder.app.models.EducationLevel
import com.universitinder.app.models.ResultMessage

data class CreateCourseUiState (
    val createLoading: Boolean = false,
    val name: String = "",
    val duration: String = "",
    val durationMenuExpanded: Boolean = false,
    val level: EducationLevel = EducationLevel.BACHELORS,
    val levelMenuExpanded: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
