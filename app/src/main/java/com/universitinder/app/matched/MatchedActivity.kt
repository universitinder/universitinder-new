package com.universitinder.app.matched

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.models.SchoolPlusImages
import com.universitinder.app.ui.theme.UniversitinderTheme
import com.universitinder.app.helpers.ActivityStarterHelper

class MatchedActivity : AppCompatActivity() {
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val school = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("school", SchoolPlusImages::class.java)
        } else {
            intent.getParcelableExtra("school")
        }

        if (school == null) finish()

        val activityStarterHelper = ActivityStarterHelper(this)
        val matchedViewModel = MatchedViewModel(school = school!!, activityStarterHelper = activityStarterHelper, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                MatchedScreen(matchedViewModel = matchedViewModel)
            }
        }
    }
}