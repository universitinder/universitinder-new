package com.universitinder.app.changePassword

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.UserController
import com.universitinder.app.ui.theme.UniversitinderTheme

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userController = UserController()
        val changePasswordViewModel = ChangePasswordViewModel(userController = userController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                ChangePasswordScreen(changePasswordViewModel = changePasswordViewModel)
            }
        }
    }
}