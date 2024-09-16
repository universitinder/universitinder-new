package com.universitinder.app.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.UserController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchesViewModel(
    private val userController: UserController,
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState : StateFlow<MatchesUiState> = _uiState.asStateFlow()

    init {
        refresh()
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

    fun startMatchedSchool() {

    }
}