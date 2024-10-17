package com.universitinder.app.school.schoolImages

import com.universitinder.app.models.ImageMetadata
import com.universitinder.app.models.ResultMessage

data class SchoolImagesUiState(
    val fetchingLoading: Boolean = false,
    val images: List<ImageMetadata> = listOf(),
    val logo: ImageMetadata? = null,
    val logoResultMessage: ResultMessage = ResultMessage(),
    val logoLoading: Boolean = false,
    val imagesResultMessage: ResultMessage = ResultMessage(),
    val imagesLoading: Boolean = false,
)
