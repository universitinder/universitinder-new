package com.universitinder.app.filters.affordability

import com.universitinder.app.models.ResultMessage

data class AffordabilityUiState(
    val provinces: String = "",
    val cities: String = "",
    val affordability: Int = 0,
    val minimum: Int = 0,
    val maximum: Int = 0,
    val courses: String = "",
    val loading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage(),
)
