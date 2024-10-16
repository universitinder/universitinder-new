package com.universitinder.app.controllers

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.storage.storage
import com.universitinder.app.file.getFileExtensionFromUri
import com.universitinder.app.models.ImageMetadata
import com.universitinder.app.models.ImagesMap
import com.universitinder.app.models.UploadImageResult
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ImageController {
    private val storage = Firebase.storage

    suspend fun getImages(documentID: String) : ImagesMap {
        val response = CompletableDeferred<ImagesMap>(null)

        coroutineScope {
            val mutableLogo = mutableStateOf<ImageMetadata?>(null)
            val imagesMutableList = mutableListOf<ImageMetadata>()
            async {
                val reference = storage.reference
                val folderRef = reference.child("schools/$documentID")
                val fileList = folderRef.listAll().await()
                for (item in fileList.items) {
                    val uri = item.downloadUrl.await()
                    if (item.name.contains("logo")) {
                        mutableLogo.value = ImageMetadata(name = item.name, uri = uri)
                    } else {
                        imagesMutableList.add(
                            ImageMetadata(name = item.name, uri = uri)
                        )
                    }
                }
            }.await()
            async {
                response.complete(
                    ImagesMap(
                        logo = mutableLogo.value ?: ImageMetadata(),
                        images = imagesMutableList.toList()
                    )
                )
            }.await()
        }

        return response.await()
    }

    suspend fun uploadLogo(context: Context, documentID: String, uri: Uri) : Boolean {
        val response = CompletableDeferred<Boolean>(null)

        val reference = storage.reference
        val extension = getFileExtensionFromUri(context, uri)
        if (extension != null) {
            val logoRef = reference.child("schools/$documentID/logo.$extension")
            logoRef.putFile(uri)
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener { response.complete(false) }
        } else {
            response.complete(false)
        }

        return response.await()
    }

    suspend fun uploadImages(context: Context, documentID: String, uris: List<Uri>) : UploadImageResult {
        val response = CompletableDeferred<UploadImageResult>(null)
        val imageMetaData = CompletableDeferred<List<ImageMetadata>>()
        val results = CompletableDeferred<List<Boolean>>()

        val reference = storage.reference
        coroutineScope {
            async {
                val resultList = mutableListOf<Boolean>()
                val imagesList = mutableListOf<ImageMetadata>()
                uris.forEach{ uri ->
                    val extension = getFileExtensionFromUri(context, uri)
                    if (extension != null) {
                        val timestamp = Timestamp.now().seconds
                        val logoRef = reference.child("schools/${documentID}/image-$timestamp.$extension")
                        imagesList.add(ImageMetadata(name = "image-${timestamp}.$extension", uri = uri))
                        logoRef.putFile(uri)
                            .addOnSuccessListener {
                                resultList.add(true)
                            }
                            .addOnFailureListener {
                                resultList.add(false)
                            }
                    } else {
                        resultList.add(false)
                    }
                }
                results.complete(resultList)
                imageMetaData.complete(imagesList)
            }.await()
            async {
                val awaitedResults = results.await()
                val awaitedImages = imageMetaData.await()
                if (awaitedResults.all { res -> res }) response.complete(UploadImageResult(successful = true, images = awaitedImages))
                else response.complete(UploadImageResult(successful = false, images = awaitedImages))
            }.await()
        }

        return response.await()
    }

    suspend fun deleteImage(documentID: String, name: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val reference = storage.reference
        coroutineScope {
            launch(Dispatchers.IO) {
                val imageRef = reference.child("schools/$documentID/$name")
                imageRef.delete()
                    .addOnSuccessListener { response.complete(true) }
                    .addOnFailureListener { response.complete(false) }
            }
        }

        return response.await()
    }
}