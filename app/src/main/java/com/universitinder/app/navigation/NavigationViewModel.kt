package com.universitinder.app.navigation

import androidx.lifecycle.ViewModel
import com.universitinder.app.home.HomeViewModel
import com.universitinder.app.models.UserState
import com.universitinder.app.profile.ProfileViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel(
    val homeViewModel: HomeViewModel,
    val profileViewModel: ProfileViewModel
): ViewModel() {
    private val _uiState = MutableStateFlow(NavigationUiState())
    val uiState : StateFlow<NavigationUiState> = _uiState.asStateFlow()

    init {
        refreshUser()
    }

    fun refreshUser() {
        val currentUser = UserState.currentUser
        if (currentUser != null) { _uiState.value = _uiState.value.copy(user = currentUser) }
    }
}