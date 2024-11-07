package com.universitinder.app.matched

import android.net.Uri
import com.universitinder.app.models.SchoolAnalytics
import com.universitinder.app.models.SchoolAnalyticsYears
import com.universitinder.app.models.SchoolPlusImages

data class MatchedUiState (
    val schoolPlusImages: SchoolPlusImages = SchoolPlusImages(),
    val logo : Uri? = null,
    val activeTab: Int = 0,
    val schoolAnalytics: SchoolAnalytics = SchoolAnalytics(),
    val schoolAnalyticsList: List<SchoolAnalytics> = listOf(),
    val selectedYear: String = SchoolAnalyticsYears.ALL.toString(),
    val lineChartLoading: Boolean = false,
)