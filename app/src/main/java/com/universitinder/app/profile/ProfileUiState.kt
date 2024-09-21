package com.universitinder.app.profile

import com.universitinder.app.models.User
import com.universitinder.app.models.UserType

data class ProfileUiState (
    val user: User = User("", "", UserType.UNKNOWN, "", ""),
    val showDeleteDialog: Boolean = false,
    val deleteDialogLoading: Boolean = false,
    val loading: Boolean = false
)
