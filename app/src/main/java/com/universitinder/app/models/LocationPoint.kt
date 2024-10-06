package com.universitinder.app.models

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationPoint(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
) : Parcelable {
    fun toGeoPoint(): GeoPoint {
        return GeoPoint(latitude, longitude)
    }

    companion object {
        fun fromGeoPoint(geoPoint: GeoPoint): LocationPoint {
            return LocationPoint(geoPoint.latitude, geoPoint.longitude)
        }
    }
}
