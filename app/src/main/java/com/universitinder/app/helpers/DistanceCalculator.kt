package com.universitinder.app.helpers

import com.universitinder.app.models.LocationPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class DistanceCalculator {
    companion object {
        fun calculateDistanceBetweenUserAndSchool(userPoint: LocationPoint, schoolPoint: LocationPoint) : Double {
            val lat1 = userPoint.latitude
            val lon1 = userPoint.longitude
            val lat2 = schoolPoint.latitude
            val lon2 = schoolPoint.longitude
            val earthRadius = 6371.0 // Radius of the Earth in kilometers
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)

            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLon / 2) * sin(dLon / 2)
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            return earthRadius * c // Distance in kilometers
        }
    }
}