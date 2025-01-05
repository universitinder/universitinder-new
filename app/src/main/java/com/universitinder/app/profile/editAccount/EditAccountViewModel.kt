package com.universitinder.app.profile.editAccount

import androidx.datastore.preferences.core.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.UserController
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.User
import com.universitinder.app.models.UserState
import com.universitinder.app.preferences.PreferencesKey
import com.universitinder.app.userDataStore
import com.universitinder.app.helpers.ActivityStarterHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditAccountViewModel(
    private val userController: UserController = UserController(),
    private val activityStarterHelper: ActivityStarterHelper,
    val popActivity: () -> Unit
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(EditAccountUiState())
    val uiState : StateFlow<EditAccountUiState> = _uiState.asStateFlow()

    init {
        if (currentUser == null)  {
            popActivity()
        } else {
            _uiState.value = _uiState.value.copy(
                email = currentUser.email,
                name = currentUser.name,
                type = currentUser.type,
                address = currentUser.address,
                contactNumber = currentUser.contactNumber,
            )
        }
    }

    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onNameChange(newVal: String) { 
        if (newVal.isEmpty() || newVal.all { !it.isDigit() }) {
            _uiState.value = _uiState.value.copy(name = newVal) 
        }
    }
    fun onAddressChange(newVal: String) { _uiState.value = _uiState.value.copy(address = newVal) }
    fun onContactNumberChange(newVal: String) {
        if (newVal.isEmpty() || newVal.all { it.isDigit() } && newVal.length <= 10) {
            _uiState.value = _uiState.value.copy(contactNumber = newVal)
        }
    }

    private fun validateContactNumber(): String? {
        val number = _uiState.value.contactNumber
        return when {
            number.isEmpty() -> "Contact number is required"
            !number.startsWith("9") -> "Contact number must start with 9"
            number.length != 10 -> "Contact number must be 10 digits"
            !number.all { it.isDigit() } -> "Contact number must contain only numbers"
            else -> null
        }
    }

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

    suspend fun updateUser() {

        val contactError = validateContactNumber()
        if (contactError != null) {
            showMessage(ResultMessageType.FAILED, contactError)
            return
        }


        if (fieldsNotFilled()) {
            showMessage(ResultMessageType.FAILED, "Please fill in all the fields")
            return
        }
        if (currentUser != null) {
            _uiState.value = _uiState.value.copy(createLoading = true)
            viewModelScope.launch {
                val newUser = User(
                    email = _uiState.value.email,
                    name = _uiState.value.name,
                    type = currentUser.type,
                    contactNumber = _uiState.value.contactNumber,
                    address = _uiState.value.address
                )
                val result = userController.updateUser(newUser)
                _uiState.value = _uiState.value.copy(createLoading = false)
                if (result) {
                    showMessage(ResultMessageType.SUCCESS, "Successfully updated account information")
                    persistUser(newUser)
                    popActivity()
                } else {
                    showMessage(ResultMessageType.FAILED, "Account information update unsuccessful")
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
}