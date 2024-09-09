package com.universitinder.app.controllers

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.universitinder.app.models.Course
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CourseController {
    private val firestore = Firebase.firestore

    suspend fun getCourse(email: String, documentID: String) : Course? {
        val response = CompletableDeferred<Course?>()

        coroutineScope {
            launch (Dispatchers.IO) {
                val coursesRef = firestore.collection("users").document(email).collection("school").document("school").collection("courses")
                coursesRef
                    .document(documentID)
                    .get()
                    .addOnSuccessListener { response.complete(it.toObject(Course::class.java)) }
                    .addOnFailureListener { response.complete(null) }
            }
        }

        return response.await()
    }

    suspend fun getCourses(email: String) : List<DocumentSnapshot> {
        val response = CompletableDeferred<List<DocumentSnapshot>>()

        coroutineScope {
            launch (Dispatchers.IO) {
                val coursesRef = firestore.collection("users").document(email).collection("school").document("school").collection("courses")
                coursesRef.get()
                    .addOnSuccessListener { response.complete(it.documents) }
                    .addOnFailureListener { response.complete(listOf()) }
            }
        }

        return response.await()
    }

    suspend fun createCourse(email: String, course: Course) : Boolean {
        val response = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val coursesRef = firestore.collection("users").document(email).collection("school").document("school").collection("courses")
                coursesRef
                    .document()
                    .set(course)
                    .addOnSuccessListener { response.complete(true) }
                    .addOnFailureListener { response.complete(false) }
            }
        }

        return response.await()
    }

    suspend fun updateCourse(email: String, documentID: String, course: Course) : Boolean {
        val response = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val coursesRef = firestore.collection("users").document(email).collection("school").document("school").collection("courses")
                coursesRef
                    .document(documentID)
                    .update(
                        "name", course.name,
                        "duration", course.duration,
                        "level", course.level
                    )
                    .addOnSuccessListener { response.complete(true) }
                    .addOnFailureListener { response.complete(false) }
            }
        }

        return response.await()
    }

    suspend fun deleteCourse(email: String, documentID: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val coursesRef = firestore.collection("users").document(email).collection("school").document("school").collection("courses")
                coursesRef
                    .document(documentID)
                    .delete()
                    .addOnSuccessListener { response.complete(true) }
                    .addOnFailureListener { response.complete(false) }
            }
        }

        return response.await()
    }

}