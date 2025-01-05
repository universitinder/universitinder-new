package com.universitinder.app.school.schoolInformationNavigation

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.universitinder.app.models.School
import com.universitinder.app.school.analytics.SchoolAnalyticsActivity
import com.universitinder.app.school.location.SchoolLocationActivity
import com.universitinder.app.school.schoolCourses.SchoolCoursesActivity
import com.universitinder.app.school.schoolFAQs.SchoolFAQsActivity
import com.universitinder.app.school.schoolImages.SchoolImagesActivity
import com.universitinder.app.school.schoolInformation.SchoolInformationActivity
import com.universitinder.app.school.schoolMissionVision.SchoolMissionVisionActivity
import com.universitinder.app.helpers.ActivityStarterHelper

class SchoolInformationNavigationViewModel(
    private val school : School,
    private val activityStarterHelper: ActivityStarterHelper,
    val popActivity: () -> Unit
): ViewModel() {

    fun startSchoolInformationActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolInformationActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolAnalyticsActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolAnalyticsActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolLocationActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolLocationActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolImagesActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolImagesActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolCoursesActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolCoursesActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolMissionVisionActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolMissionVisionActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startFAQsActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolFAQsActivity::class.java)
        intent.putExtra("SCHOOL", school)
        activityStarterHelper.startActivity(intent)
    }
}