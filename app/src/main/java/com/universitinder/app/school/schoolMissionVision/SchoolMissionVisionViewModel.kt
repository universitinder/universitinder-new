package com.universitinder.app.school.schoolMissionVision

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
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

class SchoolMissionVisionViewModel(
    private val school: School,
    private val schoolController: SchoolController,
    val popActivity: () -> Unit
) : ViewModel() {
    private val _uiState = MutableStateFlow(SchoolMissionVisionUiState())
    val uiState : StateFlow<SchoolMissionVisionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(fetchingDataLoading = true)
            }
            _uiState.value = _uiState.value.copy(
                mission = school.mission,
                vision = school.vision,
                coreValues = school.coreValues
            )
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(fetchingDataLoading = false)
            }
        }
    }

    fun onMissionChange(newVal: String) { _uiState.value = _uiState.value.copy(mission = newVal) }
    fun onVisionChange(newVal: String) { _uiState.value = _uiState.value.copy(vision = newVal) }
    fun onCoreValuesChange(newVal: String) { _uiState.value = _uiState.value.copy(coreValues = newVal) }

    private fun fieldsNotFilled() : Boolean {
        return _uiState.value.mission.isEmpty() || _uiState.value.mission.isBlank() || _uiState.value.vision.isEmpty() ||
                _uiState.value.vision.isBlank() || _uiState.value.coreValues.isEmpty() || _uiState.value.coreValues.isBlank()
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
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(setInformationLoading = true)
            }
            viewModelScope.async {
                val schoolUpdate = School(
                    mission = _uiState.value.mission,
                    vision = _uiState.value.vision,
                    coreValues = _uiState.value.coreValues,
                )
                val result = schoolController.updateSchoolMissionVision(school.documentID, schoolUpdate)
                if (result) {
                    showMessage(ResultMessageType.SUCCESS, "Successfully set institution information")
                }
                else showMessage(ResultMessageType.FAILED, "Setting institution information unsuccessful")
            }.await()
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(setInformationLoading = false)
            }
        }
    }
}