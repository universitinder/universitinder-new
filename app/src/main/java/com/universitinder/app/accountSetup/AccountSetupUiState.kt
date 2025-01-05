package com.universitinder.app.accountSetup

import com.universitinder.app.models.ResultMessage

data class AccountSetupUiState (
    val email: String = "",
    val name: String = "",
    val address: String = "",
    val contactNumber: String = "",
    val otp: String = "",
    val otpSent: Boolean = false,
    val verifyingOtp: Boolean = false,
    val createLoading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)