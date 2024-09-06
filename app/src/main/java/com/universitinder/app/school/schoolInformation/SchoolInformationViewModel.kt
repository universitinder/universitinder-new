package com.universitinder.app.school.schoolInformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.School
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolInformationViewModel(
    private val schoolController: SchoolController,
    val popActivity: () -> Unit,
): ViewModel(){
    private val _uiState = MutableStateFlow(SchoolInformationUiState())
    val uiState : StateFlow<SchoolInformationUiState> = _uiState.asStateFlow()

    init {
        val currentUser = UserState.currentUser
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(fetchingDataLoading = true)
                }
                val school = schoolController.getSchool(currentUser.email)
                if (school != null) _uiState.value = _uiState.value.copy(
                    name = school.name,
                    email = school.email,
                    contactNumber = school.contactNumber,
                    address = school.address,
                    minimum = school.minimum,
                    maximum = school.maximum,
                    affordability = school.affordability
                )
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(fetchingDataLoading = false)
                }
            }
        }
    }

    fun onNameChange(newVal: String) { _uiState.value = _uiState.value.copy(name = newVal) }
    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onContactNumberChange(newVal: String) { _uiState.value = _uiState.value.copy(contactNumber = newVal) }
    fun onAddressChange(newVal: String) { _uiState.value = _uiState.value.copy(address = newVal) }
    fun onMinimumChange(newVal: String) { _uiState.value = _uiState.value.copy(minimum = if (newVal.isEmpty() || newVal.isBlank()) 0 else newVal.toInt()) }
    fun onMaximumChange(newVal: String) { _uiState.value = _uiState.value.copy(maximum = if (newVal.isEmpty() || newVal.isBlank()) 0 else newVal.toInt()) }
    fun onAffordabilityChange(newVal: String) { _uiState.value = _uiState.value.copy(affordability = newVal.toInt()) }

    private fun fieldsNotFilled() : Boolean {
        return _uiState.value.name.isEmpty() || _uiState.value.name.isBlank() || _uiState.value.email.isEmpty() ||
                _uiState.value.email.isBlank() || _uiState.value.contactNumber.isEmpty() || _uiState.value.contactNumber.isBlank() ||
                _uiState.value.address.isEmpty() || _uiState.value.address.isBlank()
    }

    private fun validateFields() : Boolean {
        return true
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

    fun setSchoolInformation() {
        if (fieldsNotFilled()) {
            showMessage(ResultMessageType.SUCCESS, "Please fill in all required fields")
            return
        }
        if (!validateFields()) return
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(createSchoolLoading = true)
            }
            viewModelScope.async {
                val school = School(
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    contactNumber = _uiState.value.contactNumber,
                    address = _uiState.value.address,
                    minimum = _uiState.value.minimum,
                    maximum = _uiState.value.maximum,
                    affordability = _uiState.value.affordability,
                )
                val result = schoolController.updateSchool(UserState.currentUser?.email!!, school)
                if (result) {
                    showMessage(ResultMessageType.SUCCESS, "Successfully set institution information")
                }
                else showMessage(ResultMessageType.FAILED, "Setting institution information unsuccessful")
            }.await()
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(createSchoolLoading = false)
            }
        }
    }
}