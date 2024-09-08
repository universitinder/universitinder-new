package com.universitinder.app.school.schoolFAQs

import com.google.firebase.firestore.DocumentSnapshot

data class SchoolFAQsUiState(
    val fetchingLoading: Boolean = false,
    val faqs: List<DocumentSnapshot> = listOf()
)
