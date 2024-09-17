package com.universitinder.app.matches

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.controllers.UserController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.matched.MatchedActivity
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MatchesViewModel(
    private val userController: UserController,
    private val schoolController: SchoolController,
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

    fun startMatchedSchool(school: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(matchClickLoading = true) }
            val schoolPlusImage = schoolController.getSchoolPlusImageByName(school)
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