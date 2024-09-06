package com.universitinder.app.controllers

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.universitinder.app.models.School
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await

class SchoolController {
    private val firestore = Firebase.firestore

    suspend fun getSchool(email: String) : School? {
        val school = CompletableDeferred<School?>()

        firestore.collection("users")
            .document(email)
            .collection("school")
            .document(email)
            .get()
            .addOnSuccessListener {
                if (it.exists()) school.complete(it.toObject<School>())
                school.complete(null)
            }
            .addOnFailureListener { school.complete(null) }

        return school.await()
    }

    suspend fun createSchool(email: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        firestore.collection("users")
            .document(email)
            .collection("school")
            .document(email)
            .set(school)
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener { response.complete(false) }

        return response.await()
    }

    suspend fun updateSchool(email: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document(email)
        if (schoolRef.get().await().exists()) {
            firestore.collection("users")
                .document(email)
                .collection("school")
                .document(email)
                .update(
                    "name", school.name,
                    "email", school.email,
                    "contactNumber", school.contactNumber,
                    "address", school.address,
                    "minimum", school.minimum,
                    "maximum", school.maximum,
                    "affordability", school.affordability,
                    "courses", school.courses
                )
                .addOnSuccessListener {
                    response.complete(true)
                }
                .addOnFailureListener { response.complete(false) }
        } else {
            response.complete(createSchool(email = email, school = school))
        }

        return response.await()
    }

    suspend fun updateSchoolMissionVision(email: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document(email)
        if (schoolRef.get().await().exists()) {
            firestore.collection("users")
                .document(email)
                .collection("school")
                .document(email)
                .update(
                    "mission", school.mission,
                    "vision", school.vision,
                    "coreValues", school.coreValues
                )
                .addOnSuccessListener {
                    response.complete(true)
                }
                .addOnFailureListener { response.complete(false) }
        } else {
            response.complete(createSchool(email = email, school = school))
        }

        return response.await()
    }
}