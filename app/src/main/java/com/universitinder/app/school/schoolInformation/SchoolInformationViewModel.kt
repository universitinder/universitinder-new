package com.universitinder.app.school.schoolInformation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.MUNICIPALITIES_AND_CITIES
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
    private val school: School,
    private val schoolController: SchoolController,
    val popActivity: () -> Unit,
): ViewModel(){
    val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(SchoolInformationUiState())
    val uiState : StateFlow<SchoolInformationUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(fetchingDataLoading = true)
                }
                val school = schoolController.getSchoolByName(name = school.name)
                if (school != null) {
                    Log.w("VIEW MODEL", school.toString())
                    _uiState.value = _uiState.value.copy(
                        name = school.name,
                        email = school.email,
                        contactNumber = school.contactNumber,
                        link = school.link,
                        maximum = school.maximum,
                        affordability = school.affordability,
                        province = school.province,
                        municipalityOrCity = school.municipalityOrCity,
                        barangay = school.barangay,
                        street = school.street,
                        private = school.private,
                        public = school.public,
                        municipalitiesAndCities = MUNICIPALITIES_AND_CITIES[school.province]?.toList() ?: listOf()
                    )
                }
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(fetchingDataLoading = false)
                }
            }
        }
    }

    fun onNameChange(newVal: String) { _uiState.value = _uiState.value.copy(name = newVal) }
    fun onEmailChange(newVal: String) { _uiState.value = _uiState.value.copy(email = newVal) }
    fun onContactNumberChange(newVal: String) { _uiState.value = _uiState.value.copy(contactNumber = newVal) }
    fun onLinkChange(newVal: String) { _uiState.value = _uiState.value.copy(link = newVal) }
    fun onProvinceChange(newVal: String) {
        _uiState.value = _uiState.value.copy(
            province = newVal,
            municipalitiesAndCities = MUNICIPALITIES_AND_CITIES[newVal]?.toList() ?: listOf()
        )
    }
    fun onProvinceMenuExpand() { _uiState.value = _uiState.value.copy(provinceMenuExpand = true) }
    fun onProvinceMenuDismiss() { _uiState.value = _uiState.value.copy(provinceMenuExpand = false) }
    fun privateToggle() {
        _uiState.value = _uiState.value.copy(
            public = _uiState.value.private,
            private = !_uiState.value.private
        )
    }

    fun onMunicipalityOrCityChange(newVal: String) { _uiState.value = _uiState.value.copy(municipalityOrCity = newVal) }
    fun onMunicipalityOrCityMenuExpand() { _uiState.value = _uiState.value.copy(municipalityOrCityMenuExpand = true) }
    fun onMunicipalityOrCityMenuDismiss() { _uiState.value = _uiState.value.copy(municipalityOrCityMenuExpand = false) }
    fun onBarangayChange(newVal: String) { _uiState.value = _uiState.value.copy(barangay = newVal) }
    fun onStreetChange(newVal: String) { _uiState.value = _uiState.value.copy(street = newVal) }
    fun onMaximumChange(newVal: String) {
        _uiState.value = _uiState.value.copy(maximum = if (newVal.isEmpty() || newVal.isBlank()) 0 else newVal.toInt())
        determineAffordability(_uiState.value.maximum)
    }
    private fun onAffordabilityChange(newVal: Int) { _uiState.value = _uiState.value.copy(affordability = newVal) }

    private fun determineAffordability(maxVal: Int) {
        if (maxVal <= 10000) onAffordabilityChange(1)
        else if (maxVal <= 50000) onAffordabilityChange(2)
        else onAffordabilityChange(3)
    }

    private fun fieldsNotFilled() : Boolean {
        return _uiState.value.name.isEmpty() || _uiState.value.name.isBlank() || _uiState.value.email.isEmpty() ||
                _uiState.value.email.isBlank() || _uiState.value.contactNumber.isEmpty() || _uiState.value.contactNumber.isBlank()
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
                val createdSchool = School(
                    documentID = school.documentID,
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    contactNumber = _uiState.value.contactNumber,
                    private = _uiState.value.private,
                    public = _uiState.value.public,

                    maximum = _uiState.value.maximum,
                    affordability = _uiState.value.affordability,
                    province = _uiState.value.province,
                    municipalityOrCity = _uiState.value.municipalityOrCity,
                    barangay = _uiState.value.barangay,
                    street = _uiState.value.street,
                    link = _uiState.value.link
                )
                val result = schoolController.updateSchool(school.documentID, createdSchool)
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