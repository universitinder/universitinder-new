package com.universitinder.app.school.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.LocationPoint
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.School
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolLocationViewModel(
    private val school : School,
    private val schoolController: SchoolController,
    val popActivity: () -> Unit
): ViewModel() {

    private val _uiState = MutableStateFlow(SchoolLocationUiState())
    val uiState : StateFlow<SchoolLocationUiState> = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(location = LatLng(school.coordinates.latitude, school.coordinates.longitude))
    }

    fun onLocationChange(newPoint: LatLng) { _uiState.value = _uiState.value.copy(location = newPoint) }

    fun saveLocation() {
        viewModelScope.launch {
            if (_uiState.value.location != null) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(savingLoading = true) }
                val latLng = _uiState.value.location
                val result = schoolController.updateSchoolLocation(school.documentID, LocationPoint(latitude = latLng?.latitude!!, longitude = latLng.longitude))
                withContext(Dispatchers.Main) {
                    if (result) {
                        _uiState.value = _uiState.value.copy(
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully saved location"
                            ),
                            savingLoading = false
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            resultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully saved location"
                            ),
                            savingLoading = false
                        )
                    }
                }
            }
        }
    }
}