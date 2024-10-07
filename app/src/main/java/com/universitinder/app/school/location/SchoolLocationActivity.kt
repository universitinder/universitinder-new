package com.universitinder.app.school.location

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolLocationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val schoolLocationViewModel = SchoolLocationViewModel(popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                SchoolLocationScreen(schoolLocationViewModel = schoolLocationViewModel)
            }
        }
    }
}