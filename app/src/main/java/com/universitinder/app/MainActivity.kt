package com.universitinder.app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.universitinder.app.home.HomeActivity
import com.universitinder.app.login.LoginActivity
import com.universitinder.app.models.User
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import com.universitinder.app.preferences.PreferencesKey
import com.universitinder.app.ui.theme.UniversitinderTheme
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.security.MessageDigest

val Context.userDataStore by preferencesDataStore(name = "user_preferences")

@OptIn(ExperimentalStdlibApi::class)
fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(this.toByteArray())
    return digest.toHexString()
}

//private data class PopulateSchoolCourse (
//    val schools: List<School> = emptyList(),
//    val courses: List<CourseBatchHelper> = emptyList(),
//)

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

//    DO NOT TOUCH -- ONLY FOR POPULATING SCHOOL COLLECTION
//    private fun populateSchoolList(reader: CSVReader) : PopulateSchoolCourse {
//        val schoolList = mutableListOf<School>()
//        val courseList = mutableListOf<CourseBatchHelper>()
//        var nextLine: Array<String>?
//
//        while (reader.readNext().also { nextLine = it } != null) {
//            val row = nextLine!!.toList()
//            val fourYearCourses = row[8].split(",").map { CourseController.createFourYearCourse(it) }
//            val twoYearCourses = row[9].split(",").map { CourseController.createTwoYearCourse(it) }
//            val school = SchoolController.createSchoolObjectFromRow(row)
//            Log.w("MAIN ACTIVITY", school.toString())
//            schoolList.add(school)
//            courseList.add(CourseBatchHelper(school.documentID, fourYearCourses, twoYearCourses))
//        }
//
//        Log.w("MAIN ACTIVITY", schoolList.toList().toString())
//        Log.w("MAIN ACTIVITY", courseList.toList().toString())
//
//        return PopulateSchoolCourse(schoolList, courseList)
//    }

//    private suspend fun seedSchoolCollection() {
//        val assetsManager = this.assets
//        val filename = "schools-4-processed.csv"
//
//        lifecycleScope.launch {
//            try {
//                val inputStream = assetsManager.open(filename)
//                val reader = CSVReader(InputStreamReader(inputStream))
//                reader.readNext()
//
//                val populateSchoolsCourses = populateSchoolList(reader)
//
//                if (populateSchoolsCourses.schools.isNotEmpty()) {
//                    val result = SchoolController().seedDatabase(populateSchoolsCourses.schools.toList())
//                    Log.w("MAIN ACTIVITY", result.toString())
//                }
//
//                if (populateSchoolsCourses.courses.isNotEmpty()) {
//                    val resultTwo =CourseController().createCourseInBatch(populateSchoolsCourses.courses.toList())
//                    Log.w("MAIN ACTIVITY", resultTwo.toString())
//                }
//            } catch (e: IOException) {
//                Log.w("MAIN ACTIVITY EXCEPTION", e.localizedMessage!!)
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            return
        }
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
    }

    private suspend fun getUser() {
        val map : Preferences? = this.userDataStore.data.firstOrNull()
        if (map != null) {
            val user = User(
                email = map[PreferencesKey.USER_EMAIL] ?: "",
                name = map[PreferencesKey.USER_NAME] ?: "",
                contactNumber = map[PreferencesKey.USER_CONTACT_NUMBER] ?: "",
                address = map[PreferencesKey.USER_ADDRESS] ?: "",
                type = if (map[PreferencesKey.USER_TYPE] != null) UserType.valueOf(map[PreferencesKey.USER_TYPE].toString()) else UserType.UNKNOWN,
            )
            UserState.setUser(user)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

//        DO NOT UNCOMMENT UNLESS POPULATING SCHOOL COLLECTION
//        lifecycleScope.launch { seedSchoolCollection() }
        lifecycleScope.launch { getUser() }

        setContent {
            UniversitinderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column (
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}