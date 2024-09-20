package com.universitinder.app.forgotPassword

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.UserController
import com.universitinder.app.ui.theme.UniversitinderTheme

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userController = UserController()
        val forgotPasswordViewModel = ForgotPasswordViewModel(userController = userController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                ForgotPasswordScreen(forgotPasswordViewModel = forgotPasswordViewModel)
            }
        }
    }
}