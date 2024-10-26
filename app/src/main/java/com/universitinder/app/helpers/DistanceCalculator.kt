package com.universitinder.app.helpers

import com.universitinder.app.models.LocationPoint
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class DistanceCalculator {
    companion object {
        fun calculateDistanceBetweenUserAndSchool(userPoint: LocationPoint, schoolPoint: LocationPoint) : Double {
            // Extract the latitude and longitude of the user's location
            val lat1 = userPoint.latitude
            val lon1 = userPoint.longitude

            // Extract the latitude and longitude of the school's location
            val lat2 = schoolPoint.latitude
            val lon2 = schoolPoint.longitude

            // Radius of the Earth in kilometers (approximate value for distance calculations)
            val earthRadius = 6371.0

            // Calculate the difference in radians between latitudes and longitudes
            val dLat = Math.toRadians(lat2 - lat1)
            val dLon = Math.toRadians(lon2 - lon1)

            // Haversine formula - Step 1: Calculate 'a'
            // 'a' represents the square of half the chord length between the points
            // It incorporates trigonometric functions to measure distances on a sphere
            val a = sin(dLat / 2) * sin(dLat / 2) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(dLon / 2) * sin(dLon / 2)

            // Haversine formula - Step 2: Calculate 'c'
            // 'c' represents the angular distance in radians between the two points
            // atan2 is used to handle edge cases and accurately determine the angle
            val c = 2 * atan2(sqrt(a), sqrt(1 - a))

            // Multiply 'c' by the Earth's radius to convert the angular distance to kilometers
            return earthRadius * c // Distance in kilometers
        }
    }
}