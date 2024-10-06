package com.universitinder.app.school.create

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.ui.theme.UniversitinderTheme

class CreateSchoolActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schoolController = SchoolController()
        val createSchoolViewModel = CreateSchoolViewModel(schoolController = schoolController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                CreateSchoolScreen(createSchoolViewModel = createSchoolViewModel)
            }
        }
    }
}