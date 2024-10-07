package com.universitinder.app.controllers

//import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageException
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

    suspend fun getSchoolList() : List<DocumentSnapshot> {
        val schools = CompletableDeferred<List<DocumentSnapshot>>()

        coroutineScope {
            launch(Dispatchers.IO) {
                firestore.collection("schools").get()
                    .addOnSuccessListener {
                        schools.complete(it.documents)
                    }
                    .addOnFailureListener { schools.complete(emptyList()) }
            }
        }

        return schools.await()
    }

//    suspend fun getSchoolPlusImageByEmail(email: String) : SchoolPlusImages? {
//        val school = CompletableDeferred<SchoolPlusImages?>()
//        val filteredSchools = CompletableDeferred<DocumentSnapshot?>()
//
//        coroutineScope {
//            launch(Dispatchers.IO) {
//                async {
//                    firestore.collection("users").document(email).collection("school").document("school")
//                        .get()
//                        .addOnSuccessListener { objects -> filteredSchools.complete(objects) }
//                        .addOnFailureListener { filteredSchools.complete(null) }
//                }.await()
//                async {
//                    val filtered = filteredSchools.await()
//                    if (filtered == null) {
//                        school.complete(null)
//                        return@async
//                    }
//                    val storageRef = storage.reference
//                    val listOfItems = storageRef.child("users/${email}/school").listAll().await()
//                    val uris = listOfItems.items.map {
//                        val downloadURL = it.downloadUrl.await()
//                        downloadURL
//                    }
//                    school.complete(SchoolPlusImages(id = email, school = filtered.toObject(School::class.java), images = uris))
//                }.await()
//            }
//        }
//
//        return school.await()
//    }

//    suspend fun getSchoolPlusImageByDocumentId(documentId: String) : SchoolPlusImages? {
//        val school = CompletableDeferred<SchoolPlusImages?>()
//        val filteredSchools = CompletableDeferred<List<DocumentSnapshot>>()
//        Log.w("SCHOOL CONTROLLER", documentId)
//
//        coroutineScope {
//            launch(Dispatchers.IO) {
//                async {
//                    firestore.collection("schools")
//                        .whereEqualTo(FieldPath.documentId(), documentId)
//                        .limit(1)
//                        .get()
//                        .addOnSuccessListener { objects -> filteredSchools.complete(objects.documents) }
//                        .addOnFailureListener { filteredSchools.complete(emptyList()) }
//                }.await()
//                async {
//                    val filtered = filteredSchools.await()
//                    if (filtered.isEmpty()) {
//                        school.complete(null)
//                        return@async
//                    }
//                    val first = filtered.first()
//                    val id = first.reference.parent.parent?.id
//                    val storageRef = storage.reference
//                    val listOfItems = storageRef.child("users/${id}/school").listAll().await()
//                    val uris = listOfItems.items.map {
//                        val downloadURL = it.downloadUrl.await()
//                        downloadURL
//                    }
//                    school.complete(SchoolPlusImages(id = id!!, school = first.toObject(School::class.java), images = uris))
//                }.await()
//            }
//        }
//
//        return school.await()
//    }

    suspend fun getSchoolPlusImageByName(name: String) : SchoolPlusImages? {
        val school = CompletableDeferred<SchoolPlusImages?>()
        val filteredSchools = CompletableDeferred<List<DocumentSnapshot>>()

        coroutineScope {
            launch(Dispatchers.IO) {
                async {
                    firestore.collection("schools")
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
                    val id = first.reference.id
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("schools/${id}").listAll().await()
                    val uris = listOfItems.items.map {
                        val downloadURL = it.downloadUrl.await()
                        downloadURL
                    }
                    school.complete(SchoolPlusImages(id = id, school = first.toObject(School::class.java), images = uris))
                }.await()
            }
        }

        return school.await()
    }

    private fun createQueryOne(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, isPrivate: Boolean) : Query {
        return firestore.collection("schools")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("private", isPrivate)
    }
    private fun createQueryTwo(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, isPublic: Boolean) : Query {
        return firestore.collection("schools")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("public", isPublic)
    }
    private fun createQueryThree(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has2YearCourse: Boolean) : Query {
        return firestore.collection("schools")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("has2YearCourse", has2YearCourse)
    }
    private fun createQueryFour(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has3YearCourse: Boolean) : Query {
        return firestore.collection("schools")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("has3YearCourse", has3YearCourse)
    }
    private fun createQueryFive(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has4YearCourse: Boolean) : Query {
        return firestore.collection("schools")
            .whereIn("province", provinces)
            .whereIn("municipalityOrCity", cities)
            .whereArrayContainsAny("courses", courses)
            .whereEqualTo("affordability", affordability)
            .whereEqualTo("has4YearCourse", has4YearCourse)
    }
    private fun createQuerySix(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has5YearCourse: Boolean) : Query {
        return firestore.collection("schools")
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
                    val id = document.reference.id
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("schools/${id}").listAll().await()
                    async {
                        val uris = listOfItems.items.map {
                            val downloadURL = it.downloadUrl.await()
                            downloadURL
                        }
                        val schoolObject = document.toObject(School::class.java)
                        val schoolPoint = schoolObject?.coordinates!!
                        val distance = DistanceCalculator.calculateDistanceBetweenUserAndSchool(userPoint = userPoint, schoolPoint = schoolPoint)

                        Pair(SchoolPlusImages(id = id, school = document.toObject(School::class.java), images = uris), distance)
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
                val combinedSchools = firestore.collection("schools")
                    .orderBy("swipeRight", Query.Direction.DESCENDING).get().await()
                val schoolPlusImages = combinedSchools.map { document ->
                    val id = document.reference.id
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("schools/${id}").listAll().await()
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
                                id = id,
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

        firestore.collection("schools")
            .document(email)
            .get()
            .addOnSuccessListener {
                if (it.exists()) school.complete(it.toObject<School>())
                school.complete(null)
            }
            .addOnFailureListener { school.complete(null) }

        return school.await()
    }

    suspend fun getSchoolByName(name: String) : School? {
        val school = CompletableDeferred<School?>()

        coroutineScope {
            launch(Dispatchers.IO) {
                firestore.collection("schools")
                    .whereEqualTo("name", name)
                    .limit(1)
                    .get()
                    .addOnSuccessListener { objects ->
                        if (objects.isEmpty) school.complete(null)
                        val schoolObject = objects.first().toObject(School::class.java)
                        school.complete(schoolObject)
                    }
                    .addOnFailureListener { school.complete(null) }
            }
        }

        return school.await()
    }

    private suspend fun createSchool(email: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()


        firestore.collection("schools")
            .document(email)
            .set(school)
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener {
                response.complete(false)
            }

        return response.await()
    }

    suspend fun updateSchool(email: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(email)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(email)
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

        val schoolRef = firestore.collection("schools").document(email)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(email)
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

        val schoolRef = firestore.collection("schools").document(email)
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

        val schoolRef = firestore.collection("schools").document(email)
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

        val schoolRef = firestore.collection("schools").document(email)
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

        val schoolRef = firestore.collection("schools").document(email)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(email)
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

        val schoolRef = firestore.collection("schools").document(email)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(email)
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

        val schoolRef = firestore.collection("schools").document(email)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(email)
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

        val schoolRef = firestore.collection("schools").document(email)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(email)
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

    suspend fun deleteSchool(email: String) : Boolean {
        val response = CompletableDeferred<Boolean>()
        val responseTwo = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO){
                try {
                    firestore.collection("schools").document(email).collection("courses").get().await()
                        .documents.forEach {
                            firestore.collection("schools").document(email).collection("courses").document(it.id).delete().await()
                        }
                    firestore.collection("schools").document(email).collection("FAQs").get().await()
                        .documents.forEach {
                            firestore.collection("schools").document(email).collection("FAQs").document(it.id).delete().await()
                        }
                    firestore.collection("schools").document(email).delete().await()
                    response.complete(true)
                } catch (exception: FirebaseFirestoreException) {
                    response.complete(false)
                }
            }
            launch(Dispatchers.IO){
                try {
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("schools/${email}").listAll().await()
                    listOfItems.items.forEach {
                        storageRef.child(it.path).delete().await()
                    }
                    storageRef.child("schools/$email").delete().await()
                    responseTwo.complete(true)
                } catch (exception: StorageException) {
                    responseTwo.complete(false)
                }
            }
        }

        return response.await() && responseTwo.await()
    }

}