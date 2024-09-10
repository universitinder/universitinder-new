package com.universitinder.app.filters

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.universitinder.app.filters.affordability.AffordabilityFilterActivity
import com.universitinder.app.filters.city.CityFilterActivity
import com.universitinder.app.filters.courses.CoursesFilterActivity
import com.universitinder.app.filters.province.ProvinceFilterActivity
import com.universitinder.app.helpers.ActivityStarterHelper

class FiltersViewModel(
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {

    fun startProvinceActivity() {
        val intent = Intent(activityStarterHelper.getContext(), ProvinceFilterActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startCityActivity() {
        val intent = Intent(activityStarterHelper.getContext(), CityFilterActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startAffordabilityActivity() {
        val intent = Intent(activityStarterHelper.getContext(), AffordabilityFilterActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startCoursesActivity() {
        val intent = Intent(activityStarterHelper.getContext(), CoursesFilterActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }
}