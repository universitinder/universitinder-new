package com.universitinder.app.school.schoolCourses

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.models.School
import com.universitinder.app.ui.theme.UniversitinderTheme
import com.universitinder.app.helpers.ActivityStarterHelper

class SchoolCoursesActivity : AppCompatActivity() {
    private lateinit var schoolCoursesViewModel: SchoolCoursesViewModel

    override fun onRestart() {
        super.onRestart()
        schoolCoursesViewModel.refresh()
    }

    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val school = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("SCHOOL", School::class.java)
        } else {
            intent.getParcelableExtra("SCHOOL")
        }

        if (school == null) finish()

        val courseController = CourseController()
        val schoolController = SchoolController()
        val activityStarterHelper = ActivityStarterHelper(this)
        schoolCoursesViewModel = SchoolCoursesViewModel(
            school = school!!,
            courseController = courseController,
            schoolController = schoolController,
            activityStarterHelper = activityStarterHelper,
            popActivity = this::finish
        )

        setContent {
            UniversitinderTheme {
                SchoolCoursesScreen(schoolCoursesViewModel = schoolCoursesViewModel)
            }
        }
    }
}