package com.universitinder.app.forgotPassword

import com.universitinder.app.models.ResultMessage

data class ForgotPasswordUiState(
    val email: String = "",
    val loading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
