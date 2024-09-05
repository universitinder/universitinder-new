package com.universitinder.app.school.schoolCourses

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolCoursesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schoolCoursesViewModel = SchoolCoursesViewModel()

        setContent {
            UniversitinderTheme {
                SchoolCoursesScreen(schoolCoursesViewModel = schoolCoursesViewModel)
            }
        }
    }
}