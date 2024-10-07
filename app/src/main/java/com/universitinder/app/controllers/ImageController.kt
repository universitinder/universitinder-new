package com.universitinder.app.controllers

import android.content.Context
import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.universitinder.app.file.getFileExtensionFromUri
import com.universitinder.app.models.ImagesMap
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

class ImageController {
    private val storage = Firebase.storage

    suspend fun getImages(email: String) : ImagesMap {
        val response = CompletableDeferred<ImagesMap>(null)

        coroutineScope {
            val logoMutableList = mutableListOf<Uri>()
            val imagesMutableList = mutableListOf<Uri>()
            async {
                val reference = storage.reference
                val folderRef = reference.child("schools/$email")
                val fileList = folderRef.listAll().await()
                for (item in fileList.items) {
                    val uri = item.downloadUrl.await()
                    if (item.name.contains("logo")) {
                        logoMutableList.add(uri)
                    } else {
                        imagesMutableList.add(uri)
                    }
                }
            }.await()
            async {
                response.complete(
                    ImagesMap(
                        logo = logoMutableList.firstOrNull(),
                        images = imagesMutableList.toList()
                    )
                )
            }.await()
        }

        return response.await()
    }

    suspend fun uploadLogo(context: Context, email: String, uri: Uri) : Boolean {
        val response = CompletableDeferred<Boolean>(null)

        val reference = storage.reference
        val extension = getFileExtensionFromUri(context, uri)
        if (extension != null) {
            val logoRef = reference.child("schools/$email/logo.$extension")
            logoRef.putFile(uri)
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener { response.complete(false) }
        } else {
            response.complete(false)
        }

        return response.await()
    }

    suspend fun uploadImages(context: Context, email: String, uris: List<Uri>) : Boolean {
        val response = CompletableDeferred<Boolean>(null)
        val results = listOf<Boolean>()

        val reference = storage.reference
        coroutineScope {
            async {
                uris.forEachIndexed{ index, uri ->
                    val extension = getFileExtensionFromUri(context, uri)
                    if (extension != null) {
                        val logoRef = reference.child("schools/${email}/image-$index.$extension")
                        logoRef.putFile(uri)
                            .addOnSuccessListener { results.plus(true) }
                            .addOnFailureListener { results.plus(false) }
                    } else {
                        results.plus(false)
                    }
                }
            }.await()
            async {
                if (results.all { res -> res }) response.complete(true)
                else response.complete(false)
            }.await()
        }

        return response.await()
    }
}