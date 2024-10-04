package com.universitinder.app.controllers

//import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.storage
import com.universitinder.app.helpers.DistanceCalculator
import com.universitinder.app.models.CourseDurations
import com.universitinder.app.models.Filter
import com.universitinder.app.models.LocationPoint
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

    suspend fun getSchoolPlusImageByEmail(email: String) : SchoolPlusImages? {
        val school = CompletableDeferred<SchoolPlusImages?>()
        val filteredSchools = CompletableDeferred<DocumentSnapshot?>()

        coroutineScope {
            launch(Dispatchers.IO) {
                async {
                    firestore.collection("users").document(email).collection("school").document("school")
                        .get()
                        .addOnSuccessListener { objects -> filteredSchools.complete(objects) }
                        .addOnFailureListener { filteredSchools.complete(null) }
                }.await()
                async {
                    val filtered = filteredSchools.await()
                    if (filtered == null) {
                        school.complete(null)
                        return@async
                    }
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("users/${email}/school").listAll().await()
                    val uris = listOfItems.items.map {
                        val downloadURL = it.downloadUrl.await()
                        downloadURL
                    }
                    school.complete(SchoolPlusImages(id = email, school = filtered.toObject(School::class.java), images = uris))
                }.await()
            }
        }

        return school.await()
    }

    suspend fun getSchoolPlusImageByName(name: String) : SchoolPlusImages? {
        val school = CompletableDeferred<SchoolPlusImages?>()
        val filteredSchools = CompletableDeferred<List<DocumentSnapshot>>()

        coroutineScope {
            launch(Dispatchers.IO) {
                async {
                    firestore.collectionGroup("school")
                        .whereEqualTo("name", name)
                        .limit(1)
                        .get()
                        .addOnSuccessListener { objects -> filteredSchools.complete(objects.documents) }
                        .addOnFailureListener { filteredSchools.complete(emptyList()) }
                }.await()
                async {
                    val filtered = filteredSchools.await()
                    if (filtered.isEmpty()) {
                        school.complete(null)
                        return@async
                    }
                    val first = filtered.first()
                    val id = first.reference.parent.parent?.id
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("users/${id}/school").listAll().await()
                    val uris = listOfItems.items.map {
                        val downloadURL = it.downloadUrl.await()
                        downloadURL
                    }
                    school.complete(SchoolPlusImages(id = id!!, school = first.toObject(School::class.java), images = uris))
                }.await()
            }
        }

        return school.await()
    }

//    suspend fun getFilteredSchool(filter: Filter) : List<SchoolPlusImages> {
//        val schools = CompletableDeferred<List<SchoolPlusImages>>()
//        val filteredSchools = CompletableDeferred<List<DocumentSnapshot>>()
//
//        coroutineScope {
//            launch(Dispatchers.IO) {
//                async {
//                    firestore.collectionGroup("school")
//                        .get()
//                        .addOnSuccessListener { objects ->
//                            val filtered = objects.documents.filter { document ->
//                                val school = document.toObject(School::class.java)
//                                filter.provinces.contains(school?.province!!) && filter.cities.contains(school.municipalityOrCity) &&
//                                filter.courses.split("___").intersect(school.courses.toSet()).isNotEmpty()
//                            }
//                            filteredSchools.complete(filtered)
//                        }
//                        .addOnFailureListener { filteredSchools.complete(emptyList()) }
//                }.await()
//                async {
//                    val filtered = filteredSchools.await()
//                    val schoolPlusImages = filtered.map { document ->
//                        val id = document.reference.parent.parent?.id
//                        val storageRef = storage.reference
//                        val listOfItems = storageRef.child("users/${id}/school").listAll().await()
//                        async {
//                            val uris = listOfItems.items.map {
//                                val downloadURL = it.downloadUrl.await()
//                                downloadURL
//                            }
//                            SchoolPlusImages(id = id!!, school = document.toObject(School::class.java), images = uris)
//                        }.await()
//                    }
//                    schools.complete(schoolPlusImages)
//                }.await()
//            }
//        }
//
//        return schools.await()
//    }

    private fun createQueryOne(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, isPrivate: Boolean) : Query {
        return firestore.collectionGroup("school")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("private", isPrivate)
    }
    private fun createQueryTwo(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, isPublic: Boolean) : Query {
        return firestore.collectionGroup("school")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("public", isPublic)
    }
    private fun createQueryThree(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has2YearCourse: Boolean) : Query {
        return firestore.collectionGroup("school")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("has2YearCourse", has2YearCourse)
    }
    private fun createQueryFour(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has3YearCourse: Boolean) : Query {
        return firestore.collectionGroup("school")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("has3YearCourse", has3YearCourse)
    }
    private fun createQueryFive(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has4YearCourse: Boolean) : Query {
        return firestore.collectionGroup("school")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("has4YearCourse", has4YearCourse)
    }
    private fun createQuerySix(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has5YearCourse: Boolean) : Query {
        return firestore.collectionGroup("school")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("has5YearCourse", has5YearCourse)
    }

    suspend fun getFilteredSchoolTwo(filter: Filter, userPoint: LocationPoint) : List<SchoolPlusImages> {
        val sortedSchools = CompletableDeferred<List<SchoolPlusImages>>()
        val schools = CompletableDeferred<List<Pair<SchoolPlusImages, Double>>>()
        val provinces = filter.provinces.split("___").toList()
        val cities = filter.cities.split("___").toList()
        val courses = filter.courses.split("___").toList()

        coroutineScope {
            launch(Dispatchers.IO) {
                val queryOne = createQueryOne(provinces, cities, courses, filter.affordability, filter.private)
                val queryTwo = createQueryTwo(provinces, cities, courses, filter.affordability, filter.public)
                val queryThree = createQueryThree(provinces, cities, courses, filter.affordability, filter.has2YearCourse)
                val queryFour = createQueryFour(provinces, cities, courses, filter.affordability, filter.has3YearCourse)
                val queryFive = createQueryFive(provinces, cities, courses, filter.affordability, filter.has4YearCourse)
                val querySix = createQuerySix(provinces, cities, courses, filter.affordability, filter.has5YearCourse)
                val queryTasks = listOf(queryOne.get().await(), queryTwo.get().await(), queryThree.get().await(), queryFour.get().await(), queryFive.get().await(), querySix.get().await())

                val combinedSchools = queryTasks.flatMap {
                    it.documents
                }.toMutableSet()
                val schoolPlusImages = combinedSchools.map { document ->
                    val id = document.reference.parent.parent?.id
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("users/${id}/school").listAll().await()
                    async {
                        val uris = listOfItems.items.map {
                            val downloadURL = it.downloadUrl.await()
                            downloadURL
                        }
                        val schoolObject = document.toObject(School::class.java)
                        val schoolPoint = schoolObject?.coordinates!!
                        val distance = DistanceCalculator.calculateDistanceBetweenUserAndSchool(userPoint = userPoint, schoolPoint = schoolPoint)

                        Pair(SchoolPlusImages(id = id!!, school = document.toObject(School::class.java), images = uris), distance)
                    }.await()
                }
                schools.complete(schoolPlusImages)
            }
            launch {
                val awaitedSchools = schools.await()
                sortedSchools.complete(awaitedSchools.sortedBy { it.second }.map { it.first })
            }
        }

        return sortedSchools.await()
    }

    suspend fun getTopSchools(userPoint: LocationPoint) : List<SchoolPlusImages> {
        val sortedSchools = CompletableDeferred<List<SchoolPlusImages>>()
        val schools = CompletableDeferred<List<Pair<SchoolPlusImages, Double>>>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val combinedSchools = firestore.collectionGroup("school")
                    .orderBy("swipeRight", Query.Direction.DESCENDING).get().await()
                val schoolPlusImages = combinedSchools.map { document ->
                    val id = document.reference.parent.parent?.id
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("users/${id}/school").listAll().await()
                    async {
                        val uris = listOfItems.items.map {
                            val downloadURL = it.downloadUrl.await()
                            downloadURL
                        }
                        val schoolObject = document.toObject(School::class.java)
                        val schoolPoint = schoolObject.coordinates
                        val distance = DistanceCalculator.calculateDistanceBetweenUserAndSchool(
                            userPoint = userPoint,
                            schoolPoint = schoolPoint
                        )

                        Pair(
                            SchoolPlusImages(
                                id = id!!,
                                school = document.toObject(School::class.java),
                                images = uris
                            ), distance
                        )
                    }.await()
                }
                schools.complete(schoolPlusImages)
            }
            launch {
                val awaitedSchools = schools.await()
                sortedSchools.complete(awaitedSchools.sortedBy { it.second }.map { it.first })
            }
        }

        return sortedSchools.await()
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

    private suspend fun createSchool(email: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()


        firestore.collection("users")
            .document(email)
            .collection("school")
            .document("school")
            .set(school)
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener {
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
                    "isPrivate", school.isPrivate,
                    "isPublic", school.isPublic,
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

    suspend fun addSchoolSwipeRightCount(email: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        if (schoolRef.get().await().exists()) {
            schoolRef.update(
                "swipeRight", FieldValue.increment(1)
            )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener { response.complete(false) }
        } else {
            response.complete(false)
        }

        return response.await()
    }

//    suspend fun subtractSchoolSwipeRightCount(email: String) : Boolean {
//        val response = CompletableDeferred<Boolean>()
//
//        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
//        if (schoolRef.get().await().exists()) {
//            schoolRef.update(
//                "swipeRight", FieldValue.increment(-1)
//            )
//                .addOnSuccessListener { response.complete(true) }
//                .addOnFailureListener { response.complete(false) }
//        } else {
//            response.complete(false)
//        }
//
//        return response.await()
//    }

    suspend fun addSchoolSwipeLeftCount(email: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        if (schoolRef.get().await().exists()) {
            schoolRef.update(
                "swipeLeft", FieldValue.increment(1)
            )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener { response.complete(false) }
        } else {
            response.complete(false)
        }

        return response.await()
    }

//    suspend fun subtractSchoolSwipeLeftCount(email: String) : Boolean {
//        val response = CompletableDeferred<Boolean>()
//
//        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
//        if (schoolRef.get().await().exists()) {
//            schoolRef.update(
//                "swipeLeft", FieldValue.increment(-1)
//            )
//                .addOnSuccessListener { response.complete(true) }
//                .addOnFailureListener { response.complete(false) }
//        } else {
//            response.complete(false)
//        }
//
//        return response.await()
//    }

    suspend fun getSchoolDurations(email: String) : CourseDurations? {
        val response = CompletableDeferred<CourseDurations?>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        schoolRef.get()
            .addOnSuccessListener {
                val schoolObject = it.toObject(School::class.java)
                if (schoolObject == null) {
                    response.complete(null)
                    return@addOnSuccessListener
                }
                response.complete(
                    CourseDurations(
                        has2YearCourse = schoolObject.has2YearCourse,
                        has3YearCourse = schoolObject.has3YearCourse,
                        has4YearCourse = schoolObject.has4YearCourse,
                        has5YearCourse = schoolObject.has5YearCourse
                    )
                )
            }
            .addOnFailureListener {
                response.complete(null)
            }

        return response.await()
    }

    suspend fun updateSchool2YearCourse(email: String, newVal: Boolean) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        if (schoolRef.get().await().exists()) {
            firestore.collection("users")
                .document(email)
                .collection("school")
                .document("school")
                .update(
                    "has2YearCourse", newVal
                )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener {
                    response.complete(false)
                }
        } else {
            response.complete(false)
        }

        return response.await()
    }

    suspend fun updateSchool3YearCourse(email: String, newVal: Boolean) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        if (schoolRef.get().await().exists()) {
            firestore.collection("users")
                .document(email)
                .collection("school")
                .document("school")
                .update(
                    "has3YearCourse", newVal
                )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener {
                    response.complete(false)
                }
        } else {
            response.complete(false)
        }

        return response.await()
    }

    suspend fun updateSchool4YearCourse(email: String, newVal: Boolean) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        if (schoolRef.get().await().exists()) {
            firestore.collection("users")
                .document(email)
                .collection("school")
                .document("school")
                .update(
                    "has4YearCourse", newVal
                )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener {
                    response.complete(false)
                }
        } else {
            response.complete(false)
        }

        return response.await()
    }

    suspend fun updateSchool5YearCourse(email: String, newVal: Boolean) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("users").document(email).collection("school").document("school")
        if (schoolRef.get().await().exists()) {
            firestore.collection("users")
                .document(email)
                .collection("school")
                .document("school")
                .update(
                    "has5YearCourse", newVal
                )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener {
                    response.complete(false)
                }
        } else {
            response.complete(false)
        }

        return response.await()
    }
}