package com.universitinder.app.controllers

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.universitinder.app.models.School
import kotlinx.coroutines.CompletableDeferred

class SchoolController {
    private val firestore = Firebase.firestore

    private fun schoolPropExists(data: Map<String, Any>) : Boolean {
        return data["name"] != null && data["address"] != null && data["minimum"] != null &&
                data["maximum"] != null && data["affordability"] != null && data["mission"] != null &&
                data["vision"] != null && data["coreValues"] != null && data["degrees"] != null &&
                data["email"] != null && data["contactNumber"] != null
    }

    suspend fun getSchool(email: String) : School? {
        val school = CompletableDeferred<School?>()

        firestore.collection("users")
            .document(email)
            .collection("school")
            .document(email)
            .get()
            .addOnSuccessListener {
                val data = it.data
                if (it.exists() && data != null && schoolPropExists(data.toMap())) {
                    school.complete(
                        School(
                            name = data["name"] as String,
                            address = data["address"] as String,
                            minimum = data["minimum"] as Int,
                            maximum = data["maximum"] as Int,
                            affordability = data["affordability"] as Int,
                            mission = data["mission"] as String,
                            vision = data["vision"] as String,
                            coreValues = data["coreValues"] as String,
                            courses = data["courses"] as String,
                            email = data["email"] as String,
                            contactNumber = data["contactNumber"] as String
                        )
                    )
                }
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
                "mission", school.mission,
                "vision", school.vision,
                "coreValues", school.coreValues,
                "courses", school.courses
            )
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener { response.complete(false) }

        return response.await()
    }
}