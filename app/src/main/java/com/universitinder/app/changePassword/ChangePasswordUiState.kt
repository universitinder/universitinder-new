package com.universitinder.app.changePassword

import com.universitinder.app.models.ResultMessage

data class ChangePasswordUiState(
    val newPassword: String = "",
    val confirmNewPassword: String = "",
    val showPassword: Boolean = false,
    val loading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
