package com.universitinder.app.profile.editAccount

import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.UserType

data class EditAccountUiState(
    val type : UserType = UserType.UNKNOWN,
    val email: String = "",
    val name: String = "",
    val address: String = "",
    val contactNumber: String = "",
    val createLoading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
