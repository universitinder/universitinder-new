package com.universitinder.app.school.schoolMissionVision

import com.universitinder.app.models.ResultMessage

data class SchoolMissionVisionUiState(
    val mission: String = "",
    val vision: String = "",
    val coreValues: String = "",
    val fetchingDataLoading: Boolean = false,
    val setInformationLoading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage()
)
