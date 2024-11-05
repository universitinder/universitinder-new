package com.universitinder.app.home

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import com.universitinder.app.controllers.FilterController
import com.universitinder.app.controllers.SchoolController
import com.universitinder.app.controllers.UserController
import com.universitinder.app.filters.FiltersActivity
import com.universitinder.app.helpers.ActivityStarterHelper
import com.universitinder.app.helpers.DistanceCalculator
import com.universitinder.app.models.Filter
import com.universitinder.app.models.LocationPoint
import com.universitinder.app.models.School
import com.universitinder.app.models.SchoolPlusImages
import com.universitinder.app.models.UserState
import com.universitinder.app.models.UserType
import com.universitinder.app.school.profile.SchoolProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class HomeViewModel(
    private val application: Application,
    private val schoolController: SchoolController,
    private val userController: UserController,
    private val filterController: FilterController,
    private val activityStarterHelper: ActivityStarterHelper
): ViewModel() {
    private val storage = Firebase.storage
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(HomeUiState())
    private val _locationState = MutableStateFlow<Location?>(null)
    private val locationState : StateFlow<Location?> = _locationState.asStateFlow()
    val uiState : StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    private val locationRequest: LocationRequest = LocationRequest.create().apply {
        interval = 10000  // Update every 10 seconds
        fastestInterval = 5000  // Fastest update every 5 seconds
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                viewModelScope.launch {
                    _locationState.emit(location)
                    getCurrentLocation()
                    refresh()
                    stopLocationUpdates()
                }
            }
        }
    }

    fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                application,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                application,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

//    init {
//        getCurrentLocation()
//        if (currentUser != null && currentUser.type == UserType.STUDENT) {
//            refresh()
//        }
//    }

    private fun isFilterClear(filter: Filter) : Boolean {
        return filter.affordability == 0 && !filter.has5YearCourse && !filter.has3YearCourse && !filter.has4YearCourse && !filter.has2YearCourse &&
        !filter.public  && !filter.private && (filter.provinces.isEmpty() || filter.provinces.isBlank()) && (filter.cities.isEmpty() || filter.cities.isBlank()) &&
                (filter.courses.isBlank() || filter.courses.isEmpty()) && (filter.courseDuration.isEmpty() || filter.courses.isBlank())
    }

    fun refresh() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val filter = filterController.getFilter(currentUser.email)
                if (filter != null && !isFilterClear(filter)) {
                    if (locationState.value == null) return@launch
                    // GET ALL FILTERED SCHOOLS
//                    val schools = schoolController.getFilteredSchoolThree(filter = filter, LocationPoint(latitude = locationState.value?.latitude!!, longitude = locationState.value?.longitude!!))
                    val schools = schoolController.getFilteredSchoolFour(filter = filter, LocationPoint(latitude = locationState.value?.latitude!!, longitude = locationState.value?.longitude!!))
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            currentIndex = 0,
                            fetchingLoading = false,
//                            schools = schools
                            schoolsTwo = schools,
                            images = schools.map { listOf() }
                        )
                    }
                } else {
                    if (locationState.value == null) return@launch
//                    val schools = schoolController.getTopSchools(LocationPoint(latitude = locationState.value?.latitude!!, longitude = locationState.value?.longitude!!))
                    val schools = schoolController.getTopSchoolsTwo(LocationPoint(latitude = locationState.value?.latitude!!, longitude = locationState.value?.longitude!!))
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            currentIndex = 0,
                            fetchingLoading = false,
                            schoolsTwo = schools,
                            images = schools.map { listOf() }
                        )
                    }
                }
            }
        }
    }

    suspend  fun fetchImages(documentID: String) {
        val storageRef = storage.reference
        // FETCH ALL IMAGES OF SCHOOL
        val listOfItems = storageRef.child("schools/$documentID").listAll().await()
        val uris = listOfItems.items.map {
            val downloadURL = it.downloadUrl.await()
            downloadURL
        }
        val mutableList = _uiState.value.images.toMutableList()
        mutableList[_uiState.value.currentIndex] = uris
        _uiState.value = _uiState.value.copy(images = mutableList.toList())
    }

    // THIS IS THE FUNCTION WHEN SWIPING THE CARD TO RIGHT
    // HomeScreen - Line 86
    fun onSwipeRight(school: SchoolPlusImages) {
        _uiState.value = _uiState.value.copy(currentIndex = _uiState.value.currentIndex+1)
        viewModelScope.launch(Dispatchers.IO) {
            schoolController.addSchoolSwipeRightCount(school.id)
            if (currentUser != null && school.school != null) userController.addMatchedSchool(currentUser, school.school.name)
        }
    }

    // THIS IS THE FUNCTION WHEN SWIPING THE CARD TO RIGHT
    // HomeScreen - Line 86
    fun onSwipeRightTwo(school: School) {
        _uiState.value = _uiState.value.copy(currentIndex = _uiState.value.currentIndex+1)
        viewModelScope.launch(Dispatchers.IO) {
            schoolController.addSchoolSwipeRightCount(school.documentID)
            if (currentUser != null) userController.addMatchedSchool(currentUser, school.name)
        }
    }

    // THIS IS THE FUNCTION WHEN SWIPING THE CARD TO LEFT
    // HomeScreen - Line 87
    fun onSwipeLeft(id: String) {
        _uiState.value = _uiState.value.copy(currentIndex = _uiState.value.currentIndex+1)
        viewModelScope.launch(Dispatchers.IO) {
            schoolController.addSchoolSwipeLeftCount(id)
        }
    }

    fun startSchoolProfileActivity(school: SchoolPlusImages) {
        val intent = Intent(activityStarterHelper.getContext(), SchoolProfileActivity::class.java)
        intent.putExtra("school", school)
        activityStarterHelper.startActivity(intent)
    }

    fun startSchoolProfileActivityTwo(school: School) {
        val intent = Intent(activityStarterHelper.getContext(), SchoolProfileActivity::class.java)
        _uiState.value = _uiState.value.copy(middleClickLoading = true)
        viewModelScope.launch {
            val schoolPlusImages = schoolController.getSchoolPlusImageByDocumentID(school.documentID)
            intent.putExtra("school", schoolPlusImages)
            activityStarterHelper.startActivity(intent)
            _uiState.value = _uiState.value.copy(middleClickLoading = false)
        }
    }

    fun startFilterActivity() {
        val intent = Intent(activityStarterHelper.getContext(), FiltersActivity::class.java)
        activityStarterHelper.startActivity(intent)
    }

    fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                application,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                application,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                viewModelScope.launch { _locationState.emit(location) }
            }
        }
    }
}