package com.universitinder.app.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.registration.RegistrationActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState(email = "", password = "", showPassword = false))
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onPasswordChange(newVal: String) { _uiState.value = _uiState.value.copy(password = newVal) }
    fun onShowPasswordChange(newVal: Boolean) { _uiState.value = _uiState.value.copy(showPassword = newVal) }

    fun login() {

    }

    fun startRegistrationActivity() {
        val intent = Intent(activityStarterHelper.getContext(), RegistrationActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startForgotPasswordActivity() {
        TODO("Create Start Forgot Password Activity")
    }
}