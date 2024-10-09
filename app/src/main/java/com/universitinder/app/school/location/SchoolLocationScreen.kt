package com.universitinder.app.school.location

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SchoolLocationScreen(schoolLocationViewModel: SchoolLocationViewModel) {
    val uiState by schoolLocationViewModel.uiState.collectAsState()
    val fineLocationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION,
        onPermissionResult = {}
    )
    val mapProperties = remember { mutableStateOf(MapProperties(isMyLocationEnabled = true, mapType = MapType.NORMAL)) }
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(15.0794, 120.6200), 10f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Location") },
                navigationIcon = {
                    IconButton(onClick = { schoolLocationViewModel.popActivity() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    onClick = schoolLocationViewModel::saveLocation
                ) {
                    if (uiState.savingLoading) {
                        CircularProgressIndicator(color = Color.White)
                    } else {
                        if (uiState.resultMessage.show) {
                            Text(text = uiState.resultMessage.message)
                        } else {
                            Text(text = "Save Location")
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        when(fineLocationPermissionState.status) {
            PermissionStatus.Granted  -> {
                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    properties = mapProperties.value,
                    cameraPositionState = cameraPositionState,
                    onMapClick = { latLng ->
                        schoolLocationViewModel.onLocationChange(latLng)
                    }
                ) {
                    uiState.location?.let {
                        Marker(
                            state = MarkerState(position = it),
                            title = "Selected Location",
                        )
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Location Permission Required")
                    Button(onClick = { fineLocationPermissionState.launchPermissionRequest() }) {
                        Text(text = "Request Location Permission")
                    }
                }
            }
        }
    }
}