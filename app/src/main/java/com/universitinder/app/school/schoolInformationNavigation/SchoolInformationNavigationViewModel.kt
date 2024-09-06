package com.universitinder.app.school.schoolInformationNavigation

import android.content.Intent
import androidx.lifecycle.ViewModel
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.school.schoolCourses.SchoolCoursesActivity
import com.universitinder.app.school.schoolImages.SchoolImagesActivity
import com.universitinder.app.school.schoolInformation.SchoolInformationActivity
import com.universitinder.app.school.schoolMissionVision.SchoolMissionVisionActivity

class SchoolInformationNavigationViewModel(
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {

    fun startSchoolInformationActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolInformationActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolImagesActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolImagesActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolCoursesActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolCoursesActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolMissionVisionActivity() {
        val intent = Intent(activityStarterHelper.getContext(), SchoolMissionVisionActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }
}