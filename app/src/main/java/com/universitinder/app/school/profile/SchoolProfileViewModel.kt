package com.universitinder.app.school.profile

import androidx.lifecycle.ViewModel
import com.universitinder.app.models.SchoolPlusImages
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SchoolProfileViewModel (
    val school: SchoolPlusImages,
    val popActivity: () -> Unit,
): ViewModel() {
    private val _uiState = MutableStateFlow(SchoolProfileUiState())
    val uiState : StateFlow<SchoolProfileUiState> = _uiState.asStateFlow()

    init {
        if (school.school != null) _uiState.value = _uiState.value.copy(school = school.school)
        if (school.images.isNotEmpty()) {
            val logo = school.images.first { it.path!!.contains("logo") }
            val images = school.images.filter{ !(it.path!!.contains("logo")) }
            _uiState.value = _uiState.value.copy(logo = logo, images = images)
        }
    }
}