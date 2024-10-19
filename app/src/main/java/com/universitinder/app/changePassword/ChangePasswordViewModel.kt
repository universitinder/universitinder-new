package com.universitinder.app.changePassword

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

class ChangePasswordViewModel(
    private val userController: UserController,
    val popActivity: () -> Unit
): ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState : StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onNewPasswordChange(newVal: String) { _uiState.value = _uiState.value.copy(newPassword = newVal) }
    fun onConfirmNewPasswordChange(newVal: String) { _uiState.value = _uiState.value.copy(confirmNewPassword = newVal) }
    fun toggleShowPassword() { _uiState.value = _uiState.value.copy(showPassword = !_uiState.value.showPassword) }

    fun onChangePasswordClick() {
        if (_uiState.value.newPassword.isBlank() || _uiState.value.newPassword.isEmpty() || _uiState.value.confirmNewPassword.isEmpty() || _uiState.value.confirmNewPassword.isBlank()) {
            _uiState.value = _uiState.value.copy(
                resultMessage = ResultMessage(
                    type = ResultMessageType.FAILED,
                    show = true,
                    message = "Please fill in all the fields"
                )
            )
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(loading = true) }
            val result = userController.changePassword(_uiState.value.newPassword)
            withContext(Dispatchers.Main) {
                if (result) {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        resultMessage = ResultMessage(
                            show = true,
                            message = "Successfully changed your password. You can now use this next time you login",
                            type = ResultMessageType.SUCCESS
                        )
                    )
                    popActivity()
                } else {
                    _uiState.value = _uiState.value.copy(
                        loading = false,
                        resultMessage = ResultMessage(
                            show = true,
                            message = "Change password unsuccessful. Please try again later",
                            type = ResultMessageType.FAILED
                        )
                    )
                }
            }
        }
    }
}
