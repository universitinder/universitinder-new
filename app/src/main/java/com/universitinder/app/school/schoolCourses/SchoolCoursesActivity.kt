package com.universitinder.app.school.schoolCourses

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolCoursesActivity : AppCompatActivity() {
    private lateinit var schoolCoursesViewModel: SchoolCoursesViewModel

    override fun onRestart() {
        super.onRestart()
        schoolCoursesViewModel.refresh()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val courseController = CourseController()
        val activityStarterHelper = ActivityStarterHelper(this)
        schoolCoursesViewModel = SchoolCoursesViewModel(courseController = courseController, activityStarterHelper = activityStarterHelper, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                SchoolCoursesScreen(schoolCoursesViewModel = schoolCoursesViewModel)
            }
        }
    }
}