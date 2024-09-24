package com.universitinder.app.filters

import com.universitinder.app.models.ResultMessage

data class FiltersUiState(
    val provinces: List<String> = listOf(),
    val checkedProvinces: List<String> = listOf(),
    val cities: List<String> = listOf(),
    val checkedCities: List<String> = listOf(),
    val affordability: Int = 0,
    val minimum: Int = 0,
    val maximum: Int = 0,
    val courses: List<String> = listOf(),
    val checkedCourses: List<String> = listOf(),
    val privatePublic: List<String> = listOf("PUBLIC", "PRIVATE"),
    val checkedPrivatePublic: List<String> = listOf(),
    val loading: Boolean = false,
    val saveLoading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage(),
)

