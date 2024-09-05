package com.universitinder.app.navigation

import com.universitinder.app.models.User
import com.universitinder.app.models.UserType

data class NavigationUiState(
    val user: User = User("", "", UserType.UNKNOWN, "", "")
)
