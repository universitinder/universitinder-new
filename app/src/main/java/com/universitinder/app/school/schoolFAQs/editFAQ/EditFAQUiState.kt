package com.universitinder.app.school.schoolFAQs.editFAQ

import com.universitinder.app.models.ResultMessage

data class EditFAQUiState(
    val deleteLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val deleteResultMessage: ResultMessage = ResultMessage(),
    val fetchingLoading: Boolean = false,
    val question: String = "",
    val answer: String = "",
    val updateLoading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
