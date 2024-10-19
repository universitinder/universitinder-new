package com.universitinder.app.school.create

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.md5
import com.universitinder.app.models.MUNICIPALITIES_AND_CITIES
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.School
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateSchoolViewModel(
    private val schoolController: SchoolController,
    val popActivity: () -> Unit
): ViewModel() {
    private val _uiState = MutableStateFlow(CreateSchoolUiState())
    val uiState : StateFlow<CreateSchoolUiState> = _uiState.asStateFlow()

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
            private = !_uiState.value.private,
        )
    }

    fun onMunicipalityOrCityChange(newVal: String) { _uiState.value = _uiState.value.copy(municipalityOrCity = newVal) }
    fun onMunicipalityOrCityMenuExpand() { _uiState.value = _uiState.value.copy(municipalityOrCityMenuExpand = true) }
    fun onMunicipalityOrCityMenuDismiss() { _uiState.value = _uiState.value.copy(municipalityOrCityMenuExpand = false) }
    fun onBarangayChange(newVal: String) { _uiState.value = _uiState.value.copy(barangay = newVal) }
    fun onStreetChange(newVal: String) { _uiState.value = _uiState.value.copy(street = newVal) }

//    private fun fieldsNotFilled() : Boolean {
//        return _uiState.value.name.isEmpty() || _uiState.value.name.isBlank() || _uiState.value.email.isEmpty() ||
//                _uiState.value.email.isBlank() || _uiState.value.contactNumber.isEmpty() || _uiState.value.contactNumber.isBlank()
//    }
//
//    private fun validateFields() : Boolean {
//        return true
//    }

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
//        if (fieldsNotFilled()) {
//            showMessage(ResultMessageType.SUCCESS, "Please fill in all required fields")
//            return
//        }
//        if (!validateFields()) return
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(createSchoolLoading = true)
            }
            viewModelScope.async {
                val school = School(
                    documentID = _uiState.value.name.md5(),
                    name = _uiState.value.name,
                    email = _uiState.value.email,
                    contactNumber = _uiState.value.contactNumber,
                    private = _uiState.value.private,
                    public = _uiState.value.public,
                    province = _uiState.value.province,
                    municipalityOrCity = _uiState.value.municipalityOrCity,
                    barangay = _uiState.value.barangay,
                    street = _uiState.value.street,
                    link = _uiState.value.link
                )
                val result = schoolController.createSchool(school)
                if (result) {
                    showMessage(ResultMessageType.SUCCESS, "Successfully created School")
                }
                else showMessage(ResultMessageType.FAILED, "Creating new school unsuccessful")
            }.await()
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(createSchoolLoading = false)
            }
        }
    }
}