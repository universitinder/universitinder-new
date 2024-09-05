package com.universitinder.app.profile

import com.universitinder.app.models.User
import com.universitinder.app.models.UserType

data class ProfileUiState (
    val user: User = User("", "", UserType.UNKNOWN, "", ""),
    val loading: Boolean = false
)
