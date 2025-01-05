package com.universitinder.app.controllers

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestoreException
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
import com.universitinder.app.models.SchoolAnalytics
import com.universitinder.app.models.SchoolPlusImages
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import okhttp3.internal.toImmutableList

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

    suspend fun isSchoolEmail(email: String): Boolean {
        val school = firestore.collection("schools").whereEqualTo("email", email).get().await()
        return !school.isEmpty
    }

    suspend fun getSchoolByEmail(email: String): School? {
        val result = firestore.collection("schools").whereEqualTo("email", email).get().await()
        return if (result.documents.isNotEmpty()) {
            result.documents[0].toObject(School::class.java)
        } else {
            null
        }
    }

    suspend fun getSchoolPlusImageByDocumentID(documentID: String) : SchoolPlusImages? {
        val school = CompletableDeferred<SchoolPlusImages?>()
        val filteredSchools = CompletableDeferred<DocumentSnapshot?>()

        coroutineScope {
            launch(Dispatchers.IO) {
                async {
                    firestore.collection("schools")
                        .document(documentID)
                        .get()
                        .addOnSuccessListener { document -> filteredSchools.complete(document) }
                        .addOnFailureListener { filteredSchools.complete(null) }
                }.await()
                async {
                    val filtered = filteredSchools.await()
                    if (filtered == null) {
                        school.complete(null)
                        return@async
                    }
                    val id = filtered.reference.id
                    val storageRef = storage.reference
                    val listOfItems = storageRef.child("schools/${id}").listAll().await()
                    val uris = listOfItems.items.map {
                        val downloadURL = it.downloadUrl.await()
                        downloadURL
                    }
                    school.complete(SchoolPlusImages(id = id, school = filtered.toObject(School::class.java), images = uris))
                }.await()
            }
        }

        return school.await()
    }

    /** CONDITION FOR INCLUDING SCHOOL WITH SPECIFIC COURSES **/
    private fun schoolIncludeCourses(schoolCourses: List<String>, coursesFilter: List<String>) : Boolean {
        val normalizedSchoolCourses = schoolCourses.map { it.lowercase().trim() }
        val normalizedCoursesFilter = coursesFilter.map { it.lowercase().trim() }.toSet()
        return coursesFilter.isEmpty() || normalizedSchoolCourses.intersect(normalizedCoursesFilter).isNotEmpty()
    }
    /** CONDITION FOR INCLUDING SCHOOL IN SPECIFIC MUNICIPALITY OR CITY **/
    private fun schoolInMunicipalityOrCity(schoolMunicipalityOrCity: String, cities: List<String>) : Boolean {
        return cities.isEmpty() || cities.contains(schoolMunicipalityOrCity)
    }
    /** CONDITION FOR INCLUDING SCHOOL WITH SPECIFIC COURSE DURATIONS **/
    private fun schoolHasCourseDurations(school: School, has2YearCourse: Boolean, has3YearCourse: Boolean,  has4YearCourse: Boolean, has5YearCourse: Boolean) : Boolean{
        return (!has2YearCourse && !has3YearCourse && !has4YearCourse && !has5YearCourse) ||
            (school.has2YearCourse == has2YearCourse || school.has3YearCourse == has3YearCourse || school.has4YearCourse == has4YearCourse || school.has5YearCourse)
    }
    /** CONDITION FOR INCLUDING SCHOOL THAT ARE EITHER PRIVATE OR PUBLIC OR BOTH **/
    private fun schoolMatchPrivatePublic(school: School, isPrivate: Boolean, isPublic: Boolean) : Boolean {
        return (!isPublic && !isPrivate) || (school.private == isPrivate || school.public == isPublic)
    }
    /** CONDITION FOR INCLUDING SCHOOL THAT MATCH GIVEN MIN MAX **/
    private fun schoolMatchMinMax(schoolMin: Int, schoolMax: Int, filterMin: Int, filterMax: Int) : Boolean {
        // Assume that 0 or negative filter values indicate no specific min/max filtering
        val minFilterActive = filterMin > 0
        val maxFilterActive = filterMax > 0

        // Apply min/max filtering logic based on active filters
        return (!minFilterActive || schoolMin >= filterMin) &&
                (!maxFilterActive || schoolMax <= filterMax)
    }
    /** CONDITION FOR INCLUDING SCHOOL THAT MATCH GIVEN AFFORDABILITY **/
    private fun schoolMatchAffordability(schoolAffordability: Int, filterAffordability: Int) : Boolean {
        return filterAffordability == 0 || schoolAffordability == filterAffordability
    }

    /**
     * This is the main filtering function called within the home screen
     * @ HomeViewModel - Line 116
     * Input user filter and location
     * output list of SchoolPlusImages
     * **/
    suspend fun getFilteredSchoolFour(filter: Filter, userPoint: LocationPoint) : List<School> {
        val sortedSchools = CompletableDeferred<List<School>>()
        val schools = CompletableDeferred<List<Pair<School, Double>>>()
        val cities : List<String> = if (filter.cities == "") emptyList() else filter.cities.split("___").toImmutableList()
        val courses : List<String> = if (filter.courses == "") emptyList() else filter.courses.split("___").toImmutableList()

        coroutineScope {
            launch(Dispatchers.IO) {
                // FETCH ALL SCHOOLS
                val fetchedSchools = firestore.collection("schools").get().await()
                // RUN FILTER BY CALLING ALL MATCHING FUNCTIONS
                val filteredFetchedSchools = fetchedSchools.documents.filter { school ->
                    val schoolObject = school.toObject(School::class.java)

                    // Define conditions
                    val inCity = schoolInMunicipalityOrCity(schoolObject!!.municipalityOrCity, cities)
                    val hasDuration = schoolHasCourseDurations(schoolObject, filter.has2YearCourse, filter.has3YearCourse, filter.has4YearCourse, filter.has5YearCourse)
                    val matchPrivatePublic = schoolMatchPrivatePublic(schoolObject, filter.private, filter.public)
                    val includesCourses = schoolIncludeCourses(schoolObject.courses, courses)
                    val matchMinMax = schoolMatchMinMax(schoolObject.minimum, schoolObject.maximum, filter.minimum, filter.maximum)
                    val matchAffordability = schoolMatchAffordability(schoolObject.affordability, filter.affordability)

                    // At least one true condition to include, and no condition that must be false
                    val shouldInclude = listOf(inCity, hasDuration, matchPrivatePublic, includesCourses, matchMinMax, matchAffordability).any { it }

                    // Only include if at least one condition is true and none of the conditions are explicitly false
                    shouldInclude && !(listOf(inCity, hasDuration, matchPrivatePublic, includesCourses, matchMinMax, matchAffordability).contains(false))
                }
                val mappedFilteredSchools = filteredFetchedSchools.map {
                    val schoolObject = it.toObject(School::class.java)
                    val schoolPoint = schoolObject?.coordinates!!
                    // CALCULATE THE DISTANCE BETWEEN THE USER AND THE SCHOOL USING schoolPoint and userPoint
                    val distance = DistanceCalculator.calculateDistanceBetweenUserAndSchool(userPoint = userPoint, schoolPoint = schoolPoint)

                    // RETURN THE SCHOOL OBJECT WITH IMAGES
                    Pair(schoolObject, distance)
                }
                schools.complete(mappedFilteredSchools.toList())
            }
            launch {
                val awaitedSchools = schools.await()
                // SORT LIST BASED ON DISTANCE AND SWIPE RIGHTS.
                // DISTANCE IS MORE PRIORITY, THEN SWIPE RIGHTS
                // MORE SWIPE RIGHTS = TOP SCHOOL
                sortedSchools.complete(awaitedSchools.sortedWith(compareBy<Pair<School, Double>> { it.second }.thenByDescending { it.first.swipeRight }).map { it.first })
            }
        }

        return sortedSchools.await()
    }

    suspend fun getTopSchoolsTwo(userPoint: LocationPoint) : List<School> {
        val sortedSchools = CompletableDeferred<List<School>>()
        val schools = CompletableDeferred<List<Pair<School, Double>>>()

        coroutineScope {
            launch(Dispatchers.IO) {
                val combinedSchools = firestore.collection("schools").get().await()
                val schoolPlusImages = combinedSchools.map { document ->
                    val schoolObject = document.toObject(School::class.java)
                    val schoolPoint = schoolObject.coordinates
                    val distance = DistanceCalculator.calculateDistanceBetweenUserAndSchool(
                        userPoint = userPoint,
                        schoolPoint = schoolPoint
                    )

                    Pair(schoolObject, distance)
                }
                schools.complete(schoolPlusImages)
            }
            launch {
                val awaitedSchools = schools.await()
                sortedSchools.complete(awaitedSchools.sortedWith(compareBy<Pair<School, Double>> { it.second }.thenByDescending { it.first.swipeRight }).map { it.first })
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
                    "private", school.private,
                    "public", school.public,
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

    suspend fun getSchoolAnalytics(documentID: String) : List<SchoolAnalytics> {
        val response = CompletableDeferred<List<SchoolAnalytics>>()

        coroutineScope {
            launch(Dispatchers.IO) {
                firestore.collection("schools").document(documentID).collection("analytics").get()
                    .addOnSuccessListener { response.complete(it.toObjects(SchoolAnalytics::class.java)) }
                    .addOnFailureListener { response.complete(emptyList()) }
            }
        }

        return response.await()
    }

    suspend fun setSchoolAnalytics(documentID: String, schoolAnalyticsList: List<SchoolAnalytics>) : Boolean{
        val response = CompletableDeferred<Boolean>()
        val batch = firestore.batch()

        coroutineScope {
            launch(Dispatchers.IO) {
                schoolAnalyticsList.forEach {
                    val documentRef = firestore.collection("schools").document(documentID).collection("analytics").document(it.documentID)
                    batch.set(documentRef, it)
                }
                batch.commit()
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
            val collegeType = row[13]

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
                coreValues = coreValues,
                private = collegeType == "True",
                public = collegeType == "False",
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