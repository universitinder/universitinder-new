package com.universitinder.app.school.list

import com.google.firebase.firestore.DocumentSnapshot

data class SchoolListUiState(
    val fetchingLoading: Boolean = false,
    val schools: List<DocumentSnapshot> = listOf()
)
