package com.universitinder.app.school.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.LocationPoint
import com.universitinder.app.models.School
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
                val latLng = _uiState.value.location
                schoolController.updateSchoolLocation(school.email, LocationPoint(latitude = latLng?.latitude!!, longitude = latLng.longitude))
            }
        }
    }
}