package com.universitinder.app.controllers

import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.universitinder.app.models.Filter
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FilterController {
    private val firestore = Firebase.firestore

    suspend fun getFilter(email: String) : Filter? {
        val response = CompletableDeferred<Filter?>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val filtersRef = firestore.collection("users").document(email).collection("filters").document("filters")
                filtersRef.get()
                    .addOnSuccessListener { response.complete(it.toObject(Filter::class.java)) }
                    .addOnFailureListener { response.complete(null) }
            }
        }

        return response.await()
    }

    suspend fun updateFilter(email: String, filter: Filter) : Boolean {
        val response = CompletableDeferred<Boolean>(null)

        coroutineScope {
            launch(Dispatchers.IO) {
                val filtersRef = firestore.collection("users").document(email).collection("filters").document("filters")
                filtersRef.set(filter, SetOptions.merge())
                    .addOnSuccessListener { response.complete(true) }
                    .addOnFailureListener { response.complete(false) }
            }
        }

        return response.await()
    }
}