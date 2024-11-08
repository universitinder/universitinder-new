package com.universitinder.app.controllers

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import com.universitinder.app.models.Course
import com.universitinder.app.models.CourseBatchHelper
import com.universitinder.app.models.EducationLevel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CourseController {
    private val firestore = Firebase.firestore

    suspend fun getAllUniqueCourses() : List<Course> {
        val response = CompletableDeferred<List<Course>>()

        coroutineScope {
            launch(Dispatchers.IO) {
                firestore.collectionGroup("courses").get()
                    .addOnSuccessListener {
                        val courses = it.toObjects<Course>()
                        response.complete(courses.distinctBy { course -> course.name }.filter { course -> course.name != "" })
                    }
                    .addOnFailureListener { response.complete(emptyList()) }
            }
        }

        return response.await()
    }

    suspend fun getCourse(schoolID: String, documentID: String) : Course? {
        val response = CompletableDeferred<Course?>()

        coroutineScope {
            launch (Dispatchers.IO) {
                val coursesRef = firestore.collection("schools").document(schoolID).collection("courses")
                coursesRef
                    .document(documentID)
                    .get()
                    .addOnSuccessListener { response.complete(it.toObject(Course::class.java)) }
                    .addOnFailureListener { response.complete(null) }
            }
        }

        return response.await()
    }

    suspend fun getCourses(documentID: String) : List<DocumentSnapshot> {
        val response = CompletableDeferred<List<DocumentSnapshot>>()

        coroutineScope {
            launch (Dispatchers.IO) {
                val coursesRef = firestore.collection("schools").document(documentID).collection("courses")
                coursesRef.get()
                    .addOnSuccessListener { response.complete(it.documents) }
                    .addOnFailureListener { response.complete(listOf()) }
            }
        }

        return response.await()
    }

    suspend fun createCourse(documentID: String, course: Course) : Boolean {
        val firstResponse = CompletableDeferred<Boolean>()
        val secondResponse = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val coursesRef = firestore.collection("schools").document(documentID).collection("courses")
                coursesRef
                    .document()
                    .set(course)
                    .addOnSuccessListener { firstResponse.complete(true) }
                    .addOnFailureListener { firstResponse.complete(false) }
            }
            launch(Dispatchers.IO) {
                firestore.collection("schools").document(documentID)
                    .update("courses", FieldValue.arrayUnion(course.name))
                    .addOnSuccessListener { secondResponse.complete(true) }
                    .addOnSuccessListener { secondResponse.complete(false) }
            }
        }

        return firstResponse.await() && secondResponse.await()
    }

    suspend fun updateCourse(schoolID: String, documentID: String, course: Course) : Boolean {
        val response = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val coursesRef = firestore.collection("schools").document(schoolID).collection("courses")
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

    suspend fun deleteCourse(schoolID: String, documentID: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val coursesRef = firestore.collection("schools").document(schoolID).collection("courses")
                coursesRef
                    .document(documentID)
                    .delete()
                    .addOnSuccessListener { response.complete(true) }
                    .addOnFailureListener { response.complete(false) }
            }
        }

        return response.await()
    }

    suspend fun createCourseInBatch(courseBatches: List<CourseBatchHelper>) : Boolean {
        val batch = firestore.batch()
        val response = CompletableDeferred<Boolean>()
        Log.w("MAIN ACTIVITY COURSE", courseBatches.toString())

        coroutineScope {
            launch(Dispatchers.IO) {
                try {
                    async {
                        for (courseBatchHelper in courseBatches) {
                            val collectionRef = firestore.collection("schools").document(courseBatchHelper.schoolID).collection("courses")
                            for (course in courseBatchHelper.courses) {
                                val courseRef = collectionRef.document()
                                batch.set(courseRef, course)
                            }
                            for (course in courseBatchHelper.twoYearCourses) {
                                val courseRef = collectionRef.document()
                                batch.set(courseRef, course)
                            }
                        }
                    }.await()
                    async {
                        batch.commit().await()
                        Log.w("MAIN ACTIVITY", "COMMITTED COURSE BATCH")
                        response.complete(true)
                    }.await()
                } catch (e: FirebaseFirestoreException) {
                    Log.w("MAIN ACTIVITY EXCEPTION", e.localizedMessage!!)
                    response.complete(false)
                }
            }
        }

        return response.await()
    }

    companion object {
        fun createFourYearCourse(name: String) : Course {
            return Course(
                name = name.trim(),
                duration = 4,
                level = EducationLevel.BACHELORS
            )
        }

        fun createTwoYearCourse(name: String) : Course {
            return Course(
                name = name.trim(),
                duration = 2,
                level = EducationLevel.VOCATIONAL
            )
        }
    }
}