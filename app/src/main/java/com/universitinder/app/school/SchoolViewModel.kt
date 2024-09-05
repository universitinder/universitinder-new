package com.universitinder.app.school

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

class SchoolViewModel(
    private val schoolController: SchoolController
): ViewModel(){
    private val _uiState = MutableStateFlow(SchoolUiState())
    val uiState : StateFlow<SchoolUiState> = _uiState.asStateFlow()

    fun onNameChange(newVal: String) { _uiState.value = _uiState.value.copy(name = newVal) }
    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onContactNumberChange(newVal: String) { _uiState.value = _uiState.value.copy(contactNumber = newVal) }
    fun onAddressChange(newVal: String) { _uiState.value = _uiState.value.copy(address = newVal) }
    fun onMinimumChange(newVal: String) { _uiState.value = _uiState.value.copy(minimum = newVal.toInt()) }
    fun onMaximumChange(newVal: String) { _uiState.value = _uiState.value.copy(maximum = newVal.toInt()) }
    fun onAffordabilityChange(newVal: String) { _uiState.value = _uiState.value.copy(affordability = newVal.toInt()) }
    fun onMissionChange(newVal: String) { _uiState.value = _uiState.value.copy(mission = newVal) }
    fun onVisionChange(newVal: String) { _uiState.value = _uiState.value.copy(vision = newVal) }
    fun onCoreValuesChange(newVal: String) { _uiState.value = _uiState.value.copy(coreValues = newVal) }
    fun onDegreesChange(newVal: String) { _uiState.value = _uiState.value.copy(degreesString = newVal) }

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

    fun createSchool() {
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
                    mission = _uiState.value.mission,
                    vision = _uiState.value.vision,
                    coreValues = _uiState.value.coreValues,
                    courses = _uiState.value.degreesString
                )
                val result = schoolController.createSchool(UserState.currentUser?.email!!, school)
                if (result) showMessage(ResultMessageType.SUCCESS, "Successfully set institution information")
                else showMessage(ResultMessageType.FAILED, "Setting institution information unsuccessful")
            }.await()
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(createSchoolLoading = true)
            }
        }
    }
}