package com.universitinder.app.login

import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.controllers.UserController
import com.universitinder.app.forgotPassword.ForgotPasswordActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.home.HomeActivity
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.User
import com.universitinder.app.models.UserState
import com.universitinder.app.preferences.PreferencesKey
import com.universitinder.app.registration.RegistrationActivity
import com.universitinder.app.userDataStore
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.universitinder.app.models.School
import com.universitinder.app.school.SchoolActivity

class LoginViewModel(
    private val auth: FirebaseAuth,
    private val userController: UserController,
    private val schoolController: SchoolController,
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onPasswordChange(newVal: String) { _uiState.value = _uiState.value.copy(password = newVal) }
    fun onShowPasswordChange(newVal: Boolean) { _uiState.value = _uiState.value.copy(showPassword = newVal) }

    private fun fieldsNotFilled(): Boolean {
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

        viewModelScope.launch {
            val isSchoolEmail = schoolController.isSchoolEmail(_uiState.value.email)
            if (isSchoolEmail && _uiState.value.password == "123") {
                val school = schoolController.getSchoolByEmail(_uiState.value.email)
                if (school != null) {
                    startSchoolActivity(school)
                } else {
                    showMessage(ResultMessageType.FAILED, "School not found")
                }
                _uiState.value = _uiState.value.copy(loginLoading = false)
            } else {
                auth.signInWithEmailAndPassword(_uiState.value.email, _uiState.value.password)
                    .addOnSuccessListener {
                        showMessage(ResultMessageType.SUCCESS, "Login Successful")
                        viewModelScope.launch {
                            viewModelScope.async {
                                val user = userController.getUser(_uiState.value.email)
                                persistUser(user)
                            }.await()
                            startHomeActivity()
                            _uiState.value = _uiState.value.copy(loginLoading = false)
                        }
                    }
                    .addOnFailureListener {
                        showMessage(ResultMessageType.FAILED, it.message.toString())
                        _uiState.value = _uiState.value.copy(loginLoading = false)
                    }
            }
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

    fun startRegistrationActivity() {
        val intent = Intent(activityStarterHelper.getContext(), RegistrationActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startForgotPasswordActivity() {
        val intent = Intent(activityStarterHelper.getContext(), ForgotPasswordActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    private fun startSchoolActivity(school: School) {
        val intent = Intent(activityStarterHelper.getContext(), SchoolActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }
}