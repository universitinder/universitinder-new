package com.universitinder.app.login

import com.universitinder.app.models.ResultMessage

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage(),
    val loginLoading: Boolean = false
)