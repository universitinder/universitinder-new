package com.universitinder.app.school.schoolCourses.editCourse

import com.universitinder.app.models.EducationLevel
import com.universitinder.app.models.ResultMessage

data class EditCourseUiState(
    val deleteLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val deleteResultMessage: ResultMessage = ResultMessage(),
    val updateLoading: Boolean = false,
    val name: String = "",
    val duration: String = "",
    val durationMenuExpanded: Boolean = false,
    val level: EducationLevel = EducationLevel.BACHELORS,
    val levelMenuExpanded: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
