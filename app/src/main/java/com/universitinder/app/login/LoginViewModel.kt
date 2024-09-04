package com.universitinder.app.login

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.home.HomeActivity
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.registration.RegistrationActivity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val auth: FirebaseAuth,
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onPasswordChange(newVal: String) { _uiState.value = _uiState.value.copy(password = newVal) }
    fun onShowPasswordChange(newVal: Boolean) { _uiState.value = _uiState.value.copy(showPassword = newVal) }

    private fun fieldsNotFilled() : Boolean {
        return _uiState.value.email.isEmpty() || _uiState.value.password.isEmpty() ||
                 _uiState.value.email.isBlank() || _uiState.value.password.isBlank()
    }

    private fun showMessage(type: ResultMessageType, message: String) {
        _uiState.value = _uiState.value.copy(
            resultMessage = ResultMessage(
                show = true,
                type = type,
                message = message
            )
        )
    }

    fun login() {
        if (fieldsNotFilled()) {
            return showMessage(ResultMessageType.FAILED, "Please fill in all the fields")
        }

        _uiState.value = _uiState.value.copy(loginLoading = true)
        auth.signInWithEmailAndPassword(_uiState.value.email, _uiState.value.password)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(loginLoading = false)
                showMessage(ResultMessageType.SUCCESS, "Login Successful")
                startHomeActivity()
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(loginLoading = false)
                showMessage(ResultMessageType.FAILED, it.message.toString())
            }
    }

    private fun startHomeActivity() {
        val intent = Intent(activityStarterHelper.getContext(), HomeActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startRegistrationActivity() {
        val intent = Intent(activityStarterHelper.getContext(), RegistrationActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startForgotPasswordActivity() {
        TODO("Create Start Forgot Password Activity")
    }
}