package com.sun.weatherapp.data.reposiroty

import android.location.Location
import com.sun.weatherapp.data.reposiroty.source.LocationDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

class LocationRepository private constructor(
    private val locationDataSource: LocationDataSource
) {

    companion object {
        @Volatile
        private var INSTANCE: LocationRepository? = null

        fun getInstance(locationDataSource: LocationDataSource): LocationRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationRepository(locationDataSource).also { INSTANCE = it }
            }
        }
    }

    fun getCurrentLocation(listener: OnResultListener<Location>) {
        locationDataSource.getCurrentLocation(listener)
    }
}
