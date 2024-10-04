package com.universitinder.app.school

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schoolController = SchoolController()
        val schoolViewModel = SchoolViewModel(schoolController = schoolController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                SchoolScreen(schoolViewModel = schoolViewModel)
            }
        }
    }
}