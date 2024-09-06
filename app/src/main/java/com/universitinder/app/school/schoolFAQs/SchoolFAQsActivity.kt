package com.universitinder.app.school.schoolFAQs

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolFAQsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schoolFAQsViewModel = SchoolFAQsViewModel()

        setContent {
            UniversitinderTheme {
                SchoolFAQsScreen(schoolFAQsViewModel = schoolFAQsViewModel)
            }
        }
    }
}