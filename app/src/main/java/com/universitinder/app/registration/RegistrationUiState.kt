package com.universitinder.app.registration

data class RegistrationUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val showPassword: Boolean = false,
    val registrationLoading: Boolean = false
)
