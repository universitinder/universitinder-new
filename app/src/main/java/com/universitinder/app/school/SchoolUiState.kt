package com.universitinder.app.school

import android.net.Uri
import com.universitinder.app.models.SchoolAnalytics
import com.universitinder.app.models.SchoolAnalyticsYears
import com.universitinder.app.models.SchoolPlusImages

data class SchoolUiState (
    val fetchingLoading : Boolean = false,
    val schoolPlusImages: SchoolPlusImages? = null,
    val logo : Uri? = null,
    val activeTab: Int = 0,
    val showDeleteDialog: Boolean = false,
    val deleteLoading: Boolean = false,
    val schoolAnalytics: SchoolAnalytics = SchoolAnalytics(),
    val schoolAnalyticsList: List<SchoolAnalytics> = listOf(),
    val selectedYear: String = SchoolAnalyticsYears.ALL.toString(),
    val lineChartLoading: Boolean = false,
)
