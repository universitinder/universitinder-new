package com.universitinder.app.school.schoolCourses.createCourse

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.ui.theme.UniversitinderTheme

class CreateCourseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val courseController = CourseController()
        val createCourseViewModel = CreateCourseViewModel(courseController = courseController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                CreateCourseScreen(createCourseViewModel = createCourseViewModel)
            }
        }
    }
}