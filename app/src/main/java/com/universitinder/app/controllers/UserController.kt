package com.universitinder.app.controllers

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.universitinder.app.models.Filter
import com.universitinder.app.models.User
import com.universitinder.app.models.UserType
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.tasks.await

class UserController {
    private val auth : FirebaseAuth = Firebase.auth
    private val firestore : FirebaseFirestore = Firebase.firestore

    private fun userPropsExists(data: Map<String, Any>) : Boolean {
        return data["email"] != null && data["name"] != null && data["type"] != null &&
                data["address"] != null && data["contactNumber"] != null
    }

    suspend fun getUser(email: String): User? {
        val response = CompletableDeferred<User?>(null)
        firestore.collection("users").document(email).get()
            .addOnSuccessListener {
                val data = it.data
                if (it.exists() && data != null && userPropsExists(data.toMap())) {
                    val emailData  = data["email"] as String
                    val name = data["name"] as String
                    val type = UserType.valueOf(data["type"].toString())
                    val address = data["address"] as String
                    val contactNumber = data["contactNumber"] as String
                    response.complete(User(
                        email = emailData,
                        name = name,
                        type = type,
                        address = address,
                        contactNumber = contactNumber
                    ))
                }
                response.complete(null)
            }
            .addOnFailureListener { response.complete(null) }
        return response.await()
    }

    suspend fun createUser(user: User) : Boolean {
        val response = CompletableDeferred<Boolean>(null)
        firestore.collection("users").document(user.email).set(user)
            .addOnSuccessListener {
//                createSchoolDocument(user.email)
                createFiltersDocument(user.email)
                response.complete(true)
            }
            .addOnFailureListener{ response.complete(false) }
        return response.await()
    }

    private fun createFiltersDocument(email: String) {
        firestore.collection("users").document(email).collection("filters").document("filters")
            .set(Filter())
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }

//    private fun createSchoolDocument(email: String) {
//        firestore.collection("users").document(email).collection("school").document("school")
//            .set(School())
//            .addOnSuccessListener {  }
//            .addOnFailureListener {  }
//    }

    suspend fun updateUser(user: User) : Boolean {
        val response = CompletableDeferred<Boolean>(null)
        firestore.collection("users").document(user.email)
            .update(
                "name", user.name,
                "address", user.address,
                "contactNumber", user.contactNumber,
                "type", user.type.toString()
            )
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener { response.complete(false) }
        return response.await()
    }

    suspend fun addMatchedSchool(user: User, schoolName: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        firestore.collection("users").document(user.email)
            .set(mapOf("matched" to FieldValue.arrayUnion(schoolName)), SetOptions.merge())
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener { response.complete(false) }

        return response.await()
    }

    suspend fun removeMatchedSchool(user: User, schoolName: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        firestore.collection("users").document(user.email)
            .update("matched", FieldValue.arrayRemove(schoolName))
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener { response.complete(false) }

        return response.await()
    }

    suspend fun getMatchedSchools(user: User) : List<String> {
        val response = CompletableDeferred<List<String>>()

        val documentSnapshot = firestore.collection("users").document(user.email).get().await()
        if (documentSnapshot.exists()) {
            val matched = documentSnapshot.get("matched") as List<*>
            response.complete(
                matched.map {
                    it.toString()
                }
            )
        } else {
            response.complete(emptyList())
        }

        return response.await()
    }

    suspend fun sendResetPasswordEmail(email: String) : Boolean {
        val response = CompletableDeferred<Boolean>()

        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener { response.complete(false) }

        return response.await()
    }

    suspend fun changePassword(newPassword: String) : Boolean {
        val response = CompletableDeferred<Boolean>()
        val currentUser = auth.currentUser
        if (currentUser == null) response.complete(false)

        currentUser!!.updatePassword(newPassword)
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener { response.complete(false) }

        return response.await()
    }

    suspend fun deleteUser(): Boolean {
        val response = CompletableDeferred<Boolean>()
        val responseTwo = CompletableDeferred<Boolean>()
        val currentUser = auth.currentUser
        if (currentUser == null) response.complete(false)

        firestore.collection("users").document(currentUser?.email!!).delete()
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener {
                response.complete(false)
            }
        currentUser.delete()
            .addOnSuccessListener { responseTwo.complete(true) }
            .addOnFailureListener {
                responseTwo.complete(false)
            }

        return response.await() && responseTwo.await()
    }
}