package com.universitinder.app.forgotPassword

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.universitinder.app.R
import com.universitinder.app.ui.theme.UniversitinderTheme

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val forgotPasswordViewModel = ForgotPasswordViewModel(popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                ForgotPasswordScreen(forgotPasswordViewModel = forgotPasswordViewModel)
            }
        }
    }
}