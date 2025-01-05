package com.universitinder.app.school.list

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.School
import com.universitinder.app.school.SchoolActivity
import com.universitinder.app.school.create.CreateSchoolActivity
import com.universitinder.app.helpers.ActivityStarterHelper
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

    fun startRegisterSchool() {
        val intent = Intent(activityStarterHelper.getContext(), CreateSchoolActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolActivity(school: School) {
        val intent = Intent(activityStarterHelper.getContext(), SchoolActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }
}