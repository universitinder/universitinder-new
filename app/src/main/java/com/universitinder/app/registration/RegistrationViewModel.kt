package com.universitinder.app.registration

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.login.LoginActivity
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegistrationViewModel(
    private val auth: FirebaseAuth,
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {
    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState : StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onPasswordChange(newVal: String) { _uiState.value = _uiState.value.copy(password = newVal) }
    fun onConfirmPasswordChange(newVal: String) { _uiState.value = _uiState.value.copy(confirmPassword = newVal) }
    fun onShowPasswordChange(newVal: Boolean) { _uiState.value = _uiState.value.copy(showPassword = newVal) }

    fun startLoginActivity() {
        val intent = Intent(activityStarterHelper.getContext(), LoginActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    private fun fieldsNotFilled() : Boolean {
        return _uiState.value.email.isEmpty() || _uiState.value.password.isEmpty() ||
                _uiState.value.confirmPassword.isEmpty() || _uiState.value.email.isBlank() ||
                _uiState.value.password.isBlank() || _uiState.value.confirmPassword.isBlank()
    }

    private fun passwordMatching() : Boolean {
        return _uiState.value.password == _uiState.value.confirmPassword
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

    fun register() {
        if (fieldsNotFilled()) {
            return showMessage(ResultMessageType.FAILED, "Please fill in all the fields")
        }
        if (!passwordMatching()) {
            return showMessage(ResultMessageType.FAILED, "Passwords not Matching")
        }

        _uiState.value = _uiState.value.copy(registrationLoading = true)
        auth.createUserWithEmailAndPassword(_uiState.value.email, _uiState.value.password)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(registrationLoading = false)
                showMessage(ResultMessageType.SUCCESS, "Successfully registered your account")
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(registrationLoading = false)
                showMessage(ResultMessageType.FAILED, it.message.toString())
            }
    }
}