package com.universitinder.app.controllers

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.universitinder.app.models.User
import com.universitinder.app.models.UserType
import kotlinx.coroutines.CompletableDeferred

class UserController {
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
        firestore.collection("users").document(user.email).set(user.asMap())
            .addOnSuccessListener { response.complete(true) }
            .addOnFailureListener{ response.complete(false) }
        return response.await()
    }

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
}