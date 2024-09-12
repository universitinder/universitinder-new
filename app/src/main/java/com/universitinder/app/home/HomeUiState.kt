package com.universitinder.app.home

import com.universitinder.app.models.Filter
import com.universitinder.app.models.SchoolPlusImages

data class HomeUiState(
    val fetchingLoading: Boolean = false,
    val schools: List<SchoolPlusImages> = listOf(),
    val currentIndex: Int = 0,
    val filter: Filter = Filter()
)
