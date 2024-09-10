package com.universitinder.app.navigation

import androidx.lifecycle.ViewModel
import com.universitinder.app.filters.FiltersViewModel
import com.universitinder.app.home.HomeViewModel
import com.universitinder.app.models.UserState
import com.universitinder.app.profile.ProfileViewModel
import com.universitinder.app.school.SchoolViewModel
import com.universitinder.app.school.schoolInformationNavigation.SchoolInformationNavigationViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NavigationViewModel(
    val schoolInformationNavigationViewModel: SchoolInformationNavigationViewModel,
    val schoolViewModel: SchoolViewModel,
    val homeViewModel: HomeViewModel,
    val profileViewModel: ProfileViewModel,
    val filtersViewModel: FiltersViewModel
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