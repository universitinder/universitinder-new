package com.universitinder.app.filters.city

import com.universitinder.app.models.ResultMessage

data class CityFilterUiState(
    val provinces: String = "",
    val cities: List<String> = listOf(),
    val affordability: Int = 0,
    val minimum: Int = 0,
    val maximum: Int = 0,
    val courses: String = "",
    val checkedCities: List<String> = listOf(),
    val loading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage(),
)
