package com.universitinder.app.filters.province

import com.universitinder.app.models.ResultMessage

data class ProvinceFilterUiState(
    val provinces: List<String> = listOf(),
    val checkedProvinces: List<String> = listOf(),
    val loading: Boolean = false,
    val resultMessage: ResultMessage = ResultMessage(),
)
