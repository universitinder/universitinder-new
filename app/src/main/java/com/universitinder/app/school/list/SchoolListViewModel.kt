package com.universitinder.app.school.list

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.school.SchoolActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolListViewModel(
    private val schoolController: SchoolController,
    private val activityStarterHelper: ActivityStarterHelper
) : ViewModel(){
    private val _uiState = MutableStateFlow(SchoolListUiState())
    val uiState : StateFlow<SchoolListUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
            val schools = schoolController.getSchoolList()
            withContext(Dispatchers.Main) {
                _uiState.value = _uiState.value.copy(
                    schools = schools,
                    fetchingLoading = false
                )
            }
        }
    }

    fun startSchoolActivity(documentID: String) {
        val intent = Intent(activityStarterHelper.getContext(), SchoolActivity::class.java)
        intent.putExtra("DOCUMENT_ID", documentID)
        activityStarterHelper.startActivity(intent)
    }
}