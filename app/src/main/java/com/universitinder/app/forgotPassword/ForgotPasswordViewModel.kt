package com.universitinder.app.forgotPassword

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgotPasswordViewModel(
    val popActivity: () -> Unit
) : ViewModel() {
    private val auth = Firebase.auth
    private val _uiState = MutableStateFlow(ForgotPasswordUiState())
    val uiState : StateFlow<ForgotPasswordUiState> = _uiState.asStateFlow()

    fun onEmailChange(newValue: String) { _uiState.value = _uiState.value.copy(email = newValue) }

    fun sendEmail() {
        if (_uiState.value.email.isEmpty() || _uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(
                resultMessage = ResultMessage(
                    message = "Please enter your email",
                    show = true,
                    type = ResultMessageType.FAILED
                )
            )
            return
        }

        _uiState.value = _uiState.value.copy(loading = true)
        auth.sendPasswordResetEmail(_uiState.value.email)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(
                    resultMessage = ResultMessage(
                        message = "Password Reset Email Sent!",
                        show = true,
                        type = ResultMessageType.SUCCESS
                    ),
                    loading = false
                )
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(
                    resultMessage = ResultMessage(
                        message = "Failed to send password reset email",
                        show = true,
                        type = ResultMessageType.FAILED
                    ),
                    loading = false
                )
            }
    }
}