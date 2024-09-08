package com.universitinder.app.school.schoolFAQs.createFAQ

import com.universitinder.app.models.ResultMessage

data class CreateFAQUiState(
    val question: String = "",
    val answer: String = "",
    val createLoading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
