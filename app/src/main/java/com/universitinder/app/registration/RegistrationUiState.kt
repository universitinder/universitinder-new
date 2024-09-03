package com.universitinder.app.registration

import com.universitinder.app.models.ResultMessage

data class RegistrationUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val registrationLoading: Boolean = false,
//    val checkTermsAndConditions: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
