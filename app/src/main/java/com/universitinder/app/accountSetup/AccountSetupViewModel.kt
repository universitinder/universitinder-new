package com.universitinder.app.accountSetup

import android.app.Activity
import android.content.Intent
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.universitinder.app.controllers.UserController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.home.HomeActivity
import com.universitinder.app.login.LoginActivity
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.User
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import com.universitinder.app.preferences.PreferencesKey
import com.universitinder.app.userDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class AccountSetupViewModel(
    private val auth: FirebaseAuth,
    private val userController: UserController,
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {
    private val _uiState = MutableStateFlow(AccountSetupUiState())
    val uiState : StateFlow<AccountSetupUiState> = _uiState.asStateFlow()
    private var verificationId: String? = null

    init {
        if (auth.currentUser != null) {
            _uiState.value = _uiState.value.copy(email = auth.currentUser?.email!!)
        }
    }

    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onNameChange(newVal: String) { _uiState.value = _uiState.value.copy(name = newVal) }
    fun onAddressChange(newVal: String) { _uiState.value = _uiState.value.copy(address = newVal) }
    fun onContactNumberChange(newVal: String) { _uiState.value = _uiState.value.copy(contactNumber = newVal) }
    fun onOtpChange(newVal: String) { _uiState.value = _uiState.value.copy(otp = newVal) }

    private fun fieldsNotFilled() : Boolean {
        return _uiState.value.email.isEmpty() || _uiState.value.email.isBlank() || _uiState.value.name.isEmpty() ||
                _uiState.value.name.isBlank() || _uiState.value.address.isEmpty() || _uiState.value.address.isBlank() ||
                _uiState.value.contactNumber.isEmpty() || _uiState.value.contactNumber.isBlank()
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

    suspend fun createUser() {
        if (fieldsNotFilled()) {
            showMessage(ResultMessageType.FAILED, "Please fill in all the fields")
            return
        }
        _uiState.value = _uiState.value.copy(createLoading = true)
        viewModelScope.launch {
            val newUser = User(
                email = _uiState.value.email,
                name = _uiState.value.name,
                type = UserType.STUDENT,
                contactNumber = _uiState.value.contactNumber,
                address = _uiState.value.address
            )
            val result = userController.createUser(newUser)
            _uiState.value = _uiState.value.copy(createLoading = false)
            if (result) {
                showMessage(ResultMessageType.SUCCESS, "Successfully set account information")
                persistUser(newUser)
                startHomeActivity()
            } else {
                showMessage(ResultMessageType.FAILED, "Account information set unsuccessful")
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

    fun signOut() {
        auth.signOut()
        startLoginActivity()
    }

    private fun startLoginActivity() {
        val intent = Intent(activityStarterHelper.getContext(), LoginActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun sendOtp() {
        var phoneNumber = _uiState.value.contactNumber
        if (!phoneNumber.startsWith("+63")) {
            phoneNumber = "+63$phoneNumber"
        }
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activityStarterHelper.getContext() as Activity)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Auto-retrieval or instant verification
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    showMessage(ResultMessageType.FAILED, "Verification failed: ${e.message}")
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    this@AccountSetupViewModel.verificationId = verificationId
                    _uiState.value = _uiState.value.copy(otpSent = true)
                    showMessage(ResultMessageType.SUCCESS, "OTP sent to $phoneNumber")
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtp() {
        val code = _uiState.value.otp
        val verificationId = this.verificationId
        if (verificationId != null) {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            signInWithPhoneAuthCredential(credential)
        } else {
            showMessage(ResultMessageType.FAILED, "Verification ID not found")
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activityStarterHelper.getContext() as Activity) { task ->
                if (task.isSuccessful) {
                    showMessage(ResultMessageType.SUCCESS, "OTP verified successfully")
                    viewModelScope.launch { createUser() }
                } else {
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        showMessage(ResultMessageType.FAILED, "Invalid OTP")
                    } else {
                        showMessage(ResultMessageType.FAILED, "Verification failed: ${task.exception?.message}")
                    }
                }
            }
    }
}