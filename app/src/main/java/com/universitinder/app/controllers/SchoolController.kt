package com.universitinder.app.controllers

import android.net.Uri
//import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import com.universitinder.app.models.Filter
import com.universitinder.app.models.School
import com.universitinder.app.models.SchoolPlusImages
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SchoolController {
    private val firestore = Firebase.firestore
    private val storage = Firebase.storage

    suspend fun getFilteredSchool(filter: Filter) : List<SchoolPlusImages> {
//        Log.w("SCHOOL CONTROLLER", filter.toString())
        val schools = CompletableDeferred<List<SchoolPlusImages>>()
        val filteredSchools = CompletableDeferred<List<DocumentSnapshot>>()

        coroutineScope {
            launch(Dispatchers.IO) {
                async {
                    firestore.collectionGroup("school")
                        .get()
                        .addOnSuccessListener { objects ->
//                            Log.w("SCHOOL CONTROLLER", objects.documents.toString())
                            val filtered = objects.documents.filter { document ->
                                val school = document.toObject(School::class.java)
                                filter.provinces.contains(school?.province!!) && filter.cities.contains(school.municipalityOrCity) &&
                                filter.courses.split("___").intersect(school.courses.toSet()).isNotEmpty()
                            }
//                            Log.w("SCHOOL CONTROLLER", filtered.toString())
                            filteredSchools.complete(filtered)
                        }
                        .addOnFailureListener { filteredSchools.complete(emptyList()) }
                }.await()
                async {
                    val filtered = filteredSchools.await()
                    val schoolPlusImages = filtered.map { document ->
                        val uris = mutableListOf<Uri>()
                        val id = document.reference.parent.parent?.id
                        val storageRef = storage.reference
                        val listOfItems = storageRef.child("users/${id}").listAll().await()
                        listOfItems.items.forEach {
                            val downloadURL = it.downloadUrl.await()
                            uris.plus(downloadURL)
                        }

                        SchoolPlusImages(school = document.toObject(School::class.java), images = uris)
                    }
//                    Log.w("SCHOOL CONTROLLER", schoolPlusImages.toString())
                    schools.complete(schoolPlusImages)
                }.await()
            }
        }

        return schools.await()
    }

    suspend fun getSchool(email: String) : School? {
        val school = CompletableDeferred<School?>()

        firestore.collection("users")
            .document(email)
            .collection("school")
            .document("school")
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
            .document("school")
            .set(school)
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener {
//                Log.w("SCHOOL CONTROLLER", it.message!!)
                response.complete(false)
            }

        return response.await()
    }

    suspend fun updateSchool(email: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        if (schoolRef.get().await().exists()) {
            firestore.collection("users")
                .document(email)
                .collection("school")
                .document("school")
                .update(
                    "name", school.name,
                    "email", school.email,
                    "contactNumber", school.contactNumber,
                    "address", school.address,
                    "minimum", school.minimum,
                    "maximum", school.maximum,
                    "affordability", school.affordability,
                    "province", school.province,
                    "municipalityOrCity", school.municipalityOrCity,
                    "barangay", school.barangay,
                    "street", school.street,
                    "link", school.link
                )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener {
//                    Log.w("SCHOOL CONTROLLER", it.message!!)
                    response.complete(false)
                }
        } else {
            response.complete(createSchool(email = email, school = school))
        }

        return response.await()
    }

    suspend fun updateSchoolMissionVision(email: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        if (schoolRef.get().await().exists()) {
            firestore.collection("users")
                .document(email)
                .collection("school")
                .document("school")
                .update(
                    "mission", school.mission,
                    "vision", school.vision,
                    "coreValues", school.coreValues
                )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener { response.complete(false) }
        } else {
            response.complete(createSchool(email = email, school = school))
        }

        return response.await()
    }
}