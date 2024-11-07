package com.universitinder.app.school.analytics

import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.SchoolAnalytics
import com.universitinder.app.models.SchoolAnalyticsYears

data class SchoolAnalyticsUiState(
    val saveLoading: Boolean = false,
    val fetchLoading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage(),
    val firstYear: SchoolAnalytics = SchoolAnalytics(year = SchoolAnalyticsYears.FIRST),
    val secondYear: SchoolAnalytics = SchoolAnalytics(year = SchoolAnalyticsYears.SECOND),
    val thirdYear: SchoolAnalytics = SchoolAnalytics(year = SchoolAnalyticsYears.THIRD),
    val fourthYear: SchoolAnalytics = SchoolAnalytics(year = SchoolAnalyticsYears.FOURTH),
    val fifthYear: SchoolAnalytics = SchoolAnalytics(year = SchoolAnalyticsYears.FIFTH),
)
