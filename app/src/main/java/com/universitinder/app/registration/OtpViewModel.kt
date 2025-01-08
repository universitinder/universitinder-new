package com.universitinder.app.registration

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class OtpUiState(
    val isLoading: Boolean = false,
    val errorMessage: String = ""
)

class OtpViewModel(private val context: Context) : ViewModel() {
    private val _uiState = MutableStateFlow(OtpUiState())
    val uiState: StateFlow<OtpUiState> = _uiState.asStateFlow()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().reference

    fun createAccount(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                result.user?.let { user ->
                    user.sendEmailVerification().await()

                    val userData = hashMapOf(
                        "email" to email,
                        "uid" to user.uid,
                        "createdAt" to ServerValue.TIMESTAMP,
                        "isVerified" to false
                    )

                    database.child("users")
                        .child(user.uid)
                        .setValue(userData)
                        .await()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Verification email sent. Please check your inbox."
                    )

                    // Notify success and delay before navigating to login
                    onSuccess() // Close the current activity
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Registration failed"
                )
            }
        }
    }

    // Function to update the loading state
    fun updateLoadingState(isLoading: Boolean) {
        _uiState.value = _uiState.value.copy(isLoading = isLoading)
    }
}

