package com.universitinder.app.school.schoolImages

import android.net.Uri
import com.universitinder.app.models.ResultMessage

data class SchoolImagesUiState(
    val fetchingLoading: Boolean = false,
    val images: List<Uri> = listOf(),
    val logo: Uri? = null,
    val logoResultMessage: ResultMessage = ResultMessage(),
    val logoLoading: Boolean = false,
    val imagesResultMessage: ResultMessage = ResultMessage(),
    val imagesLoading: Boolean = false,
)
