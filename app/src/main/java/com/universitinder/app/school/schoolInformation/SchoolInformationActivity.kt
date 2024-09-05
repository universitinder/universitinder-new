package com.universitinder.app.school.schoolInformation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolInformationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schoolController = SchoolController()
        val schoolInformationViewModel = SchoolInformationViewModel(schoolController = schoolController)

        setContent {
            UniversitinderTheme {
                SchoolInformationScreen(schoolInformationViewModel = schoolInformationViewModel)
            }
        }
    }
}