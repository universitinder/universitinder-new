package com.universitinder.app.school.schoolImages

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.ImageController
import com.universitinder.app.models.ImageMetadata
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.School
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SchoolImagesViewModel(
    private val school: School,
    private val imageController: ImageController,
    val popActivity: () -> Unit,
): ViewModel() {
    private val _uiState = MutableStateFlow(SchoolImagesUiState())
    val uiState : StateFlow<SchoolImagesUiState> = _uiState.asStateFlow()

    private fun onImagesPicked(newImages: List<ImageMetadata>) { _uiState.value = _uiState.value.copy(images = _uiState.value.images + newImages) }
    private fun onLogoPicked(uri: ImageMetadata?) { _uiState.value = _uiState.value.copy(logo = uri) }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
            val currentUser = UserState.currentUser
            if (currentUser != null) {
                val imagesMap = imageController.getImages(school.documentID)
                onLogoPicked(imagesMap.logo)
                onImagesPicked(imagesMap.images)
            }
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = false) }
        }
    }

    fun saveLogo(context: Context, logo: Uri) {
        val currentUser = UserState.currentUser

        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(logoLoading = true) }
                val result = imageController.uploadLogo(context = context, documentID = school.documentID, uri = logo)
                if (result) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            logo = ImageMetadata(name = logo.pathSegments.last(), uri = logo),
                            logoResultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully uploaded logo"
                            ),
                            logoLoading = false
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            logoResultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.FAILED,
                                message = "Logo upload unsuccessful"
                            ),
                            logoLoading = false
                        )
                    }
                }
            }
        }
    }

    fun saveImages(context: Context, images: List<Uri>) {
        val currentUser = UserState.currentUser

        if (images.isEmpty()) return
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(imagesLoading = true) }
                val result = imageController.uploadImages(context = context, documentID = school.documentID, uris = images)
                Log.w("VIEW MODEL", result.images.toString())
                if (result.successful) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            images = _uiState.value.images + result.images,
                            imagesResultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully uploaded images"
                            ),
                            imagesLoading = false
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            imagesResultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.FAILED,
                                message = "images upload unsuccessful"
                            ),
                            imagesLoading = false
                        )
                    }
                }
            }
        }
    }

    fun removeImage(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(imagesLoading = true) }
            val result = imageController.deleteImage(school.documentID, name)
            withContext(Dispatchers.Main) {
                if (result) {
                    _uiState.value = _uiState.value.copy(
                        images = _uiState.value.images.filter { it.name != name },
                        imagesLoading = false
                    )
                }
            }
        }
    }
}