package com.universitinder.app.matches

data class MatchesUiState(
    val fetchingLoading: Boolean = false,
    val matches: List<String> = emptyList(),
    val matchClickLoading: Boolean = false,
)
