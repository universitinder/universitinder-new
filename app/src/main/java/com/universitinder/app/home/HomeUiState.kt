package com.universitinder.app.home

import android.net.Uri
import com.universitinder.app.models.Filter
import com.universitinder.app.models.School
import com.universitinder.app.models.SchoolPlusImages

data class HomeUiState(
    val fetchingLoading: Boolean = false,
    val schools: List<SchoolPlusImages> = listOf(),
    val schoolsTwo: List<School> = listOf(),
    val images: List<List<Uri>> = listOf(),
    val middleClickLoading: Boolean = false,
    val currentIndex: Int = -1,
    val filter: Filter = Filter(),
    val isLocationEnabled: Boolean = true
)
