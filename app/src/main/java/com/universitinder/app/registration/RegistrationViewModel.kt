package com.universitinder.app.registration

import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.universitinder.app.controllers.UserController
import com.universitinder.app.home.HomeActivity
import com.universitinder.app.login.LoginActivity
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.User
import com.universitinder.app.models.UserState
import com.universitinder.app.preferences.PreferencesKey
import com.universitinder.app.userDataStore
import com.universitinder.app.helpers.ActivityStarterHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.app.Activity

class RegistrationViewModel(
    private val auth: FirebaseAuth,
    private val userController: UserController,
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

    private fun validatePassword(password: String): Boolean {
        if (password.length < 12) return false // Minimum 12 characters

        val hasUppercase = password.any { it.isUpperCase() }
        val hasLowercase = password.any { it.isLowerCase() }
        val hasSpecialChar = password.any { !it.isLetterOrDigit() }

        return hasUppercase && hasLowercase && hasSpecialChar
    }

    fun register() {
        if (fieldsNotFilled()) {
            return showMessage(ResultMessageType.FAILED, "Please fill in all the fields")
        }
        if (!passwordMatching()) {
            return showMessage(ResultMessageType.FAILED, "Passwords not Matching")
        }
        if (!validatePassword(_uiState.value.password)) {
            return showMessage(ResultMessageType.FAILED, "Password requirements not met: 1 uppercase, 1 lowercase, 1 special character, 12 characters")
        }
    
        val intent = Intent(activityStarterHelper.getContext(), OtpActivity::class.java).apply {
            putExtra("email", _uiState.value.email)
            putExtra("password", _uiState.value.password)
        }
        
        activityStarterHelper.launchActivityForResult(intent) { resultCode ->
            if (resultCode == Activity.RESULT_OK) {
                // Check if email is verified before completing registration
                val user = auth.currentUser
                if (user?.isEmailVerified == true) {
                    completeRegistration()
                } else {
                    showMessage(ResultMessageType.FAILED, "Email verification required")
                }
            }
        }
    }
    
    private fun completeRegistration() {
        _uiState.value = _uiState.value.copy(registrationLoading = true)
        auth.createUserWithEmailAndPassword(_uiState.value.email, _uiState.value.password)
            .addOnSuccessListener {
                _uiState.value = _uiState.value.copy(registrationLoading = false)
                showMessage(ResultMessageType.SUCCESS, "Successfully registered your account")
                viewModelScope.launch(Dispatchers.IO) {
                    viewModelScope.async {
                        val user = userController.getUser(_uiState.value.email)
                        persistUser(user)
                    }.await()
                    startHomeActivity()
                    withContext(Dispatchers.Main) { 
                        _uiState.value = _uiState.value.copy(registrationLoading = false) 
                    }
                }
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(registrationLoading = false)
                showMessage(ResultMessageType.FAILED, it.message.toString())
            }
    }

    private suspend fun persistUser(user: User?) {
        UserState.setUser(user)
        if (user != null) {
            activityStarterHelper.getContext().userDataStore.edit { preferences ->
                preferences[PreferencesKey.USER_EMAIL] = user.email
                preferences[PreferencesKey.USER_NAME] = user.name
                preferences[PreferencesKey.USER_CONTACT_NUMBER] = user.contactNumber
                preferences[PreferencesKey.USER_ADDRESS] = user.address
                preferences[PreferencesKey.USER_TYPE] = user.type.toString()
            }
        }
    }
    
    private fun startHomeActivity() {
        val intent = Intent(activityStarterHelper.getContext(), HomeActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }
}
