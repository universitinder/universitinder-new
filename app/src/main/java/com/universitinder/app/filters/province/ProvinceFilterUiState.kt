package com.universitinder.app.filters.province

import com.universitinder.app.models.ResultMessage

data class ProvinceFilterUiState(
    val provinces: List<String> = listOf(),
    val cities: String = "",
    val affordability: Int = 0,
    val minimum: Int = 0,
    val maximum: Int = 0,
    val courses: String = "",
    val checkedProvinces: List<String> = listOf(),
    val loading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage(),
)
