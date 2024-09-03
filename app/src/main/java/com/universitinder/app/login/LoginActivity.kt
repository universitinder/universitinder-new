package com.universitinder.app.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.ui.theme.UniversitinderTheme

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        val activityStarterHelper = ActivityStarterHelper(this)
        val loginViewModel = LoginViewModel(auth = auth, activityStarterHelper = activityStarterHelper)

        setContent {
            UniversitinderTheme {
                LoginScreen(loginViewModel = loginViewModel)
            }
        }
    }
}