package com.universitinder.app.school.schoolCourses.editCourse

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.CourseController
import com.universitinder.app.ui.theme.UniversitinderTheme

class EditCourseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val documentID = intent.getStringExtra("documentID") ?: ""
        if (documentID == "") finish()

        val courseController = CourseController()
        val editCourseViewModel = EditCourseViewModel(documentID = documentID, courseController = courseController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                EditCourseScreen(editCourseViewModel = editCourseViewModel)
            }
        }
    }
}