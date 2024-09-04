package com.universitinder.app.home

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.universitinder.app.MainActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import kotlinx.coroutines.launch

class HomeViewModel(
    private val auth: FirebaseAuth,
    private val activityStarterHelper: ActivityStarterHelper,
    private val clearUser: suspend () -> Unit
): ViewModel() {

    fun logout() {
        auth.signOut()
        viewModelScope.launch {
            clearUser()
        }
        val intent = Intent(activityStarterHelper.getContext(), MainActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }
}