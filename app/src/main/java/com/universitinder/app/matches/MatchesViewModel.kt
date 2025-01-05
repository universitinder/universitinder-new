package com.universitinder.app.matches

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.controllers.UserController
import com.universitinder.app.matched.MatchedActivity
import com.universitinder.app.md5
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.universitinder.app.helpers.ActivityStarterHelper

class MatchesViewModel(
    private val userController: UserController,
    private val schoolController: SchoolController,
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState : StateFlow<MatchesUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null && currentUser.type == UserType.STUDENT) {
            refresh()
        }
    }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val schools = userController.getMatchedSchools(currentUser)
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = false, matches = schools) }

            }
        }
    }

    fun removeMatch(school: String) {
        if (currentUser != null) {
            viewModelScope.launch {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true)}
                val removed = userController.removeMatchedSchool(currentUser, school)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(fetchingLoading = false)
                    if (removed)
                        _uiState.value = _uiState.value.copy(matches = _uiState.value.matches.filter { it != school })
                }
            }
        }
    }

    fun startMatchedSchool(school: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(matchClickLoading = true) }
            val schoolPlusImage = schoolController.getSchoolPlusImageByDocumentID(school.md5())
            if (schoolPlusImage == null) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(matchClickLoading = false) }
                return@launch
            }
            val intent = Intent(activityStarterHelper.getContext(), MatchedActivity::class.java)
            intent.putExtra("school", schoolPlusImage)
            activityStarterHelper.startActivity(intent)
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(matchClickLoading = false) }
        }
    }
}