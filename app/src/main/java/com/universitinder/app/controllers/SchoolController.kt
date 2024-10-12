package com.universitinder.app.controllers

import android.util.Log
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
import com.universitinder.app.md5
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

//    suspend fun getSchoolPlusImageByEmail(documentID: String) : SchoolPlusImages? {
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

//    private fun createQueryOne(provinces: List<String>, cities: List<String>) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//    }
//    private fun createQueryTwo(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, isPublic: Boolean) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//            .whereArrayContainsAny("courses", courses)
//    }
//    private fun createQueryThree(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has2YearCourse: Boolean) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//            .whereEqualTo("has2YearCourse", has2YearCourse)
//    }
//    private fun createQueryFour(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has3YearCourse: Boolean) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//            .whereEqualTo("has3YearCourse", has3YearCourse)
//    }
//    private fun createQueryFive(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has4YearCourse: Boolean) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//            .whereEqualTo("has4YearCourse", has4YearCourse)
//    }
//    private fun createQuerySix(provinces: List<String>, cities: List<String>, courses: List<String>, affordability: Int, has5YearCourse: Boolean) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//            .whereEqualTo("has5YearCourse", has5YearCourse)
//    }
//    private fun createQuerySeven(provinces: List<String>, cities: List<String>, courses: List<String>) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//            .whereArrayContainsAny("courses", courses)
//    }
//    private fun createQueryEight(provinces: List<String>, cities: List<String>, isPrivate: Boolean) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//            .whereEqualTo("private", isPrivate)
//    }
//    private fun createQueryNine(provinces: List<String>, cities: List<String>, isPublic: Boolean) : Query {
//        return firestore.collection("schools")
//            .whereIn("province", provinces)
//            .whereIn("municipalityOrCity", cities)
//            .whereEqualTo("public", isPublic)
//    }

//    suspend fun getFilteredSchoolTwo(filter: Filter, userPoint: LocationPoint) : List<SchoolPlusImages> {
//        val sortedSchools = CompletableDeferred<List<SchoolPlusImages>>()
//        val schools = CompletableDeferred<List<Pair<SchoolPlusImages, Double>>>()
//        val provinces = filter.provinces.split("___").toList()
//        val cities = filter.cities.split("___").toList()
//        val courses = filter.courses.split("___").toList()
//        Log.w("SCHOOL CONTROLLER", filter.toString())
//
//        coroutineScope {
//            launch(Dispatchers.IO) {
//                val queryOne = createQueryOne(provinces, cities)
//                val queryTwo = createQueryTwo(provinces, cities, courses, filter.affordability, filter.public)
//                val queryThree = createQueryThree(provinces, cities, courses, filter.affordability, filter.has2YearCourse)
//                val queryFour = createQueryFour(provinces, cities, courses, filter.affordability, filter.has3YearCourse)
//                val queryFive = createQueryFive(provinces, cities, courses, filter.affordability, filter.has4YearCourse)
//                val querySix = createQuerySix(provinces, cities, courses, filter.affordability, filter.has5YearCourse)
//                val queryTasks = listOf(queryOne.get().await(), queryTwo.get().await(), queryThree.get().await(), queryFour.get().await(), queryFive.get().await(), querySix.get().await())
//
//                val combinedSchools = queryTasks.flatMap {
//                    it.documents
//                }.toMutableSet()
//                val schoolPlusImages = combinedSchools.map { document ->
//                    val id = document.reference.id
//                    val storageRef = storage.reference
//                    val listOfItems = storageRef.child("schools/${id}").listAll().await()
//                    async {
//                        val uris = listOfItems.items.map {
//                            val downloadURL = it.downloadUrl.await()
//                            downloadURL
//                        }
//                        val schoolObject = document.toObject(School::class.java)
//                        val schoolPoint = schoolObject?.coordinates!!
//                        val distance = DistanceCalculator.calculateDistanceBetweenUserAndSchool(userPoint = userPoint, schoolPoint = schoolPoint)
//
//                        Pair(SchoolPlusImages(id = id, school = document.toObject(School::class.java), images = uris), distance)
//                    }.await()
//                }
//                schools.complete(schoolPlusImages)
//            }
//            launch {
//                val awaitedSchools = schools.await()
//                sortedSchools.complete(awaitedSchools.sortedBy { it.second }.map { it.first })
//            }
//        }
//
//        return sortedSchools.await()
//    }

    private fun schoolIncludeCourses(schoolCourses: List<String>, coursesFilter: List<String>) : Boolean {
        return schoolCourses.intersect(coursesFilter.toSet()).isNotEmpty()
    }
    private fun schoolInMunicipalityOrCity(schoolMunicipalityOrCity: String, cities: List<String>) : Boolean {
        return cities.contains(schoolMunicipalityOrCity)
    }
    private fun schoolHasCourseDurations(school: School, has2YearCourse: Boolean, has3YearCourse: Boolean,  has4YearCourse: Boolean, has5YearCourse: Boolean) : Boolean{
        return school.has2YearCourse == has2YearCourse || school.has3YearCourse == has3YearCourse || school.has4YearCourse == has4YearCourse || school.has5YearCourse || has5YearCourse
    }
    private fun schoolMatchPrivatePublic(school: School, isPrivate: Boolean, isPublic: Boolean) : Boolean {
        return school.isPrivate == isPrivate || school.isPublic == isPublic
    }
    private fun schoolMatchAffordability(schoolAffordability: Int, filterAffordability: Int) : Boolean {
        return schoolAffordability == filterAffordability
    }

    suspend fun getFilteredSchoolThree(filter: Filter, userPoint: LocationPoint) : List<SchoolPlusImages> {
        val sortedSchools = CompletableDeferred<List<SchoolPlusImages>>()
        val schools = CompletableDeferred<List<Pair<SchoolPlusImages, Double>>>()
        val cities = filter.cities.split("___").toList()
        val courses = filter.courses.split("___").toList()

        coroutineScope {
            launch(Dispatchers.IO) {
                val fetchedSchools = firestore.collection("schools").get().await()
                val filteredFetchedSchools = fetchedSchools.documents.filter {
                    val schoolObject = it.toObject(School::class.java)

                    schoolInMunicipalityOrCity(schoolObject?.municipalityOrCity!!, cities) && (schoolHasCourseDurations(schoolObject, filter.has2YearCourse, filter.has3YearCourse, filter.has4YearCourse, filter.has5YearCourse) ||
                            schoolMatchPrivatePublic(schoolObject, filter.private, filter.public) || schoolIncludeCourses(schoolObject.courses, courses) || schoolMatchAffordability(schoolObject.affordability, filter.affordability))
                }
                val schoolPlusImages = filteredFetchedSchools.map { document ->
                    val schoolObject = document.toObject(School::class.java)
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("schools/${schoolObject?.documentID}").listAll().await()
                    async {
                        val uris = listOfItems.items.map {
                            val downloadURL = it.downloadUrl.await()
                            downloadURL
                        }
                        val schoolPoint = schoolObject?.coordinates!!
                        val distance = DistanceCalculator.calculateDistanceBetweenUserAndSchool(userPoint = userPoint, schoolPoint = schoolPoint)

                        Pair(SchoolPlusImages(id = schoolObject.documentID, school = document.toObject(School::class.java), images = uris), distance)
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
                    val schoolObject = document.toObject(School::class.java)
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("schools/${schoolObject.documentID}").listAll().await()
                    async {
                        val uris = listOfItems.items.map {
                            val downloadURL = it.downloadUrl.await()
                            downloadURL
                        }
                        val schoolPoint = schoolObject.coordinates
                        val distance = DistanceCalculator.calculateDistanceBetweenUserAndSchool(
                            userPoint = userPoint,
                            schoolPoint = schoolPoint
                        )

                        Pair(
                            SchoolPlusImages(
                                id = schoolObject.documentID,
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

    suspend fun getSchool(documentID: String) : School? {
        val school = CompletableDeferred<School?>()

        firestore.collection("schools")
            .document(documentID)
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

    suspend fun createSchool(school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        firestore.collection("schools")
            .document(school.documentID)
            .set(school)
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener {
                response.complete(false)
            }

        return response.await()
    }

    suspend fun updateSchool(documentID: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(documentID)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(documentID)
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
        }

        return response.await()
    }

    suspend fun updateSchoolMissionVision(documentID: String, school: School) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(documentID)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(documentID)
                .update(
                    "mission", school.mission,
                    "vision", school.vision,
                    "coreValues", school.coreValues
                )
                .addOnSuccessListener { response.complete(true) }
                .addOnFailureListener { response.complete(false) }
        }

        return response.await()
    }

    suspend fun addSchoolSwipeRightCount(documentID: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(documentID)
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

//    suspend fun subtractSchoolSwipeRightCount(documentID: String) : Boolean {
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

    suspend fun addSchoolSwipeLeftCount(documentID: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(documentID)
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

//    suspend fun subtractSchoolSwipeLeftCount(documentID: String) : Boolean {
//        val response = CompletableDeferred<Boolean>()
//
//        val schoolRef = firestore.collection("users").document(documentID).collection("school").document("school")
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

    suspend fun getSchoolDurations(documentID: String) : CourseDurations? {
        val response = CompletableDeferred<CourseDurations?>()

        val schoolRef = firestore.collection("schools").document(documentID)
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

    suspend fun updateSchool2YearCourse(documentID: String, newVal: Boolean) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(documentID)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(documentID)
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

    suspend fun updateSchool3YearCourse(documentID: String, newVal: Boolean) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(documentID)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(documentID)
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

    suspend fun updateSchool4YearCourse(documentID: String, newVal: Boolean) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(documentID)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(documentID)
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

    suspend fun updateSchool5YearCourse(documentID: String, newVal: Boolean) : Boolean {
        val response = CompletableDeferred<Boolean>()

        val schoolRef = firestore.collection("schools").document(documentID)
        if (schoolRef.get().await().exists()) {
            firestore.collection("schools")
                .document(documentID)
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

    suspend fun deleteSchool(documentID: String) : Boolean {
        val response = CompletableDeferred<Boolean>()
        val responseTwo = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO){
                try {
                    firestore.collection("schools").document(documentID).collection("courses").get().await()
                        .documents.forEach {
                            firestore.collection("schools").document(documentID).collection("courses").document(it.id).delete().await()
                        }
                    firestore.collection("schools").document(documentID).collection("FAQs").get().await()
                        .documents.forEach {
                            firestore.collection("schools").document(documentID).collection("FAQs").document(it.id).delete().await()
                        }
                    firestore.collection("schools").document(documentID).delete().await()
                    response.complete(true)
                } catch (exception: FirebaseFirestoreException) {
                    response.complete(false)
                }
            }
            launch(Dispatchers.IO){
                try {
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("schools/${documentID}").listAll().await()
                    listOfItems.items.forEach {
                        storageRef.child(it.path).delete().await()
                    }
                    storageRef.child("schools/$documentID").delete().await()
                    responseTwo.complete(true)
                } catch (exception: StorageException) {
                    responseTwo.complete(false)
                }
            }
        }

        return response.await() && responseTwo.await()
    }

    suspend fun updateSchoolLocation(documentID: String, locationPoint: LocationPoint) : Boolean {
        val response = CompletableDeferred<Boolean>()

        coroutineScope {
            launch(Dispatchers.IO)  {
                firestore.collection("schools").document(documentID)
                    .update("coordinates", locationPoint)
                    .addOnSuccessListener { response.complete(true) }
                    .addOnFailureListener { response.complete(false) }
            }
        }

        return response.await()
    }

    companion object {
        fun createSchoolObjectFromRow(row: List<String>): School {
            val nameOfSchool = row[0]
            val email = row[1]
            val link = row[2]
            val city = row[3]
            val barangay = row[4]
            val street = row[5]
            val minimum = if (row[6].isEmpty() || row[6].isBlank() || row[6] == "Free") {
                0
            } else {
                row[6].toInt()
            }
            val maximum = if (row[7].isEmpty() || row[7].isBlank() || row[7] == "Free") {
                0
            } else {
                row[7].toInt()
            }
            val courses = row[8].split(",")
            val twoYearCourses = row[9].split(",")
            val mission = row[10]
            val vision = row[11]
            val coreValues = row[12]

            return School(
                documentID = nameOfSchool.md5(),
                name = nameOfSchool,
                email = email,
                link = link,
                province = "Pampanga",
                municipalityOrCity = city,
                barangay = barangay,
                street = street,
                minimum = minimum,
                maximum = maximum,
                affordability = determineAffordability(maximum),
                courses = courses + twoYearCourses,
                has2YearCourse = twoYearCourses.isNotEmpty(),
                has4YearCourse = courses.isNotEmpty(),
                mission = mission,
                vision = vision,
                coreValues = coreValues
            )
        }
        private fun determineAffordability(maxVal: Int) : Int {
            if (maxVal == 0) return 0
            else if (maxVal <= 10000) return 1
            else if (maxVal <= 50000) return 2
            return 3
        }
    }

    suspend fun seedDatabase(schools: List<School>) : Boolean {
        val firestore = Firebase.firestore
        val batchRef = firestore.batch()
        val response = CompletableDeferred<Boolean>()
        Log.w("MAIN ACTIVITY SCHOOLS", schools.toString())

        coroutineScope {
            launch(Dispatchers.IO) {
                try {
                    async {
                        val collectionRef = firestore.collection("schools")
                        for (school in schools) {
                            val documentRef = collectionRef.document(school.documentID)
                            batchRef.set(documentRef, school)
                        }
                    }.await()
                    async {
                        batchRef.commit().await()
                        Log.w("MAIN ACTIVITY", "COMMITTED SCHOOL BATCH")
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
}