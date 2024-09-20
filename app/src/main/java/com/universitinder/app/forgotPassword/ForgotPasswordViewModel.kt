package com.universitinder.app.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.UserController
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ForgotPasswordViewModel(
    private val userController: UserController,
    val popActivity: () -> Unit
) : ViewModel() {
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

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = true) }
            val result = userController.sendResetPasswordEmail(_uiState.value.email)
            withContext(Dispatchers.Main) {
                if (result) {
                    _uiState.value = _uiState.value.copy(
                        resultMessage = ResultMessage(
                            message = "Password Reset Email Sent!",
                            show = true,
                            type = ResultMessageType.SUCCESS
                        ),
                        loading = false
                    )
                } else {
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
    }
}