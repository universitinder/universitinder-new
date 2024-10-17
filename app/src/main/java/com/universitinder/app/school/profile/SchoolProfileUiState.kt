package com.universitinder.app.school.profile

import android.net.Uri
import com.universitinder.app.models.School

data class SchoolProfileUiState(
    val school: School = School(),
    val logo: Uri? = null,
    val images: List<Uri> = emptyList()
)
