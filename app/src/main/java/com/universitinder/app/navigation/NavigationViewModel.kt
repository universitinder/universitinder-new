package com.universitinder.app.navigation

import androidx.lifecycle.ViewModel
import com.universitinder.app.home.HomeViewModel
import com.universitinder.app.profile.ProfileViewModel

class NavigationViewModel(
    val homeViewModel: HomeViewModel,
    val profileViewModel: ProfileViewModel
): ViewModel() {
}