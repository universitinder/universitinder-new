package com.universitinder.app.filters.province

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.ui.theme.UniversitinderTheme

class ProvinceFilterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val filterController = FilterController()
        val provinceFilterViewModel = ProvinceFilterViewModel(filterController = filterController, popActivity = this::finish)

        setContent {
            UniversitinderTheme {
                ProvinceFilterScreen(provinceFilterViewModel = provinceFilterViewModel)
            }
        }
    }
}