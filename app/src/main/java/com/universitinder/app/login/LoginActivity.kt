package com.universitinder.app.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.ui.theme.UniversitinderTheme

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityStarterHelper = ActivityStarterHelper(this)
        val loginViewModel = LoginViewModel(activityStarterHelper = activityStarterHelper)

        setContent {
            UniversitinderTheme {
                LoginScreen(loginViewModel = loginViewModel)
            }
        }
    }
}