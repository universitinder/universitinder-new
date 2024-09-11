package com.universitinder.app.filters.courses

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.ui.theme.UniversitinderTheme

class CoursesFilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filterController = FilterController()
        val courseController = CourseController()
        val coursesFilterViewModel = CoursesFilterViewModel(filterController = filterController, courseController = courseController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                CoursesFilterScreen(coursesFilterViewModel = coursesFilterViewModel)
            }
        }
    }
}