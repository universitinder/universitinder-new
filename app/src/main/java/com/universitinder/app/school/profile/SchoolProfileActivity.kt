package com.universitinder.app.school.profile

import android.os.Build
import android.os.Build.VERSION
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.models.SchoolPlusImages
import com.universitinder.app.ui.theme.UniversitinderTheme

class SchoolProfileActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val school = if (VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("school", SchoolPlusImages::class.java)
        } else {
            intent.getParcelableExtra("school")
        }

        if (school == null) finish()

        val schoolProfileViewModel = SchoolProfileViewModel(school = school!!, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                SchoolProfileScreen(schoolProfileViewModel = schoolProfileViewModel)
            }
        }
    }
}