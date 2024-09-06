package com.universitinder.app.school.schoolInformation

import com.universitinder.app.models.ResultMessage

data class SchoolInformationUiState (
    val name: String = "",
    val email: String = "",
    val contactNumber: String = "",
    val address: String = "",
    val minimum: Int = 0,
    val maximum: Int = 0,
    val affordability: Int = 0,
    val fetchingDataLoading: Boolean = false,
    val createSchoolLoading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
