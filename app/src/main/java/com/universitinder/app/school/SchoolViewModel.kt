package com.universitinder.app.school

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolViewModel(
    private val schoolController: SchoolController
): ViewModel(){
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(SchoolUiState())
    val uiState : StateFlow<SchoolUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val schoolPlusImages = schoolController.getSchoolPlusImageByEmail(currentUser.email)
                if (schoolPlusImages == null) {
                    withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = false) }
                    return@launch
                }
                val logo = schoolPlusImages.images.first { it.lastPathSegment!!.split("/")[3].contains("logo") }
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        fetchingLoading = false,
                        schoolPlusImages = schoolPlusImages,
                        logo = logo
                    )
                }
            }
        }
    }

    fun onTabChange(newVal: Int) { _uiState.value = _uiState.value.copy(activeTab = newVal) }
}