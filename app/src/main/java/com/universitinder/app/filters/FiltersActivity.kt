package com.universitinder.app.filters

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.ui.theme.UniversitinderTheme

class FiltersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val activityStarterHelper = ActivityStarterHelper(this)
        val filtersViewModel = FiltersViewModel(activityStarterHelper = activityStarterHelper)

        setContent {
            UniversitinderTheme {
                FiltersScreen(filtersViewModel = filtersViewModel)
            }
        }
    }
}