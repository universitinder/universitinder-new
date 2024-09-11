package com.universitinder.app.filters.city

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.ui.theme.UniversitinderTheme

class CityFilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filterController = FilterController()
        val cityFilterViewModel = CityFilterViewModel(filterController = filterController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                CityFilterScreen(cityFilterViewModel = cityFilterViewModel)
            }
        }
    }
}