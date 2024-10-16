package com.universitinder.app.models

import android.net.Uri

data class ImageMetadata(
    val name: String = "",
    val uri: Uri? = null
)

data class UploadImageResult(
    val successful: Boolean = false,
    val images: List<ImageMetadata> = emptyList()
)

class ImagesMap(
    val logo: ImageMetadata,
    val images: List<ImageMetadata>
)