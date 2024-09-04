package com.universitinder.app.accountSetup

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.universitinder.app.controllers.UserController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.ui.theme.UniversitinderTheme

class AccountSetupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userController = UserController()
        val activityStarterHelper = ActivityStarterHelper(this)
        val accountSetupViewModel = AccountSetupViewModel(auth = Firebase.auth, userController = userController, activityStarterHelper = activityStarterHelper)

        setContent {
            UniversitinderTheme {
                AccountSetupScreen(accountSetupViewModel = accountSetupViewModel)
            }
        }
    }
}