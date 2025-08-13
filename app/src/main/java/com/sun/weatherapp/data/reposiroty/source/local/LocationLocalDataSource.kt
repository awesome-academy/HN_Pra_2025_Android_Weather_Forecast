package com.sun.weatherapp.data.reposiroty.source.local

import android.location.Location
import com.sun.weatherapp.data.reposiroty.source.LocationDataSource
import com.sun.weatherapp.data.reposiroty.source.LocationService
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

class LocationLocalDataSource private constructor(
    private val locationService: LocationService
) : LocationDataSource {

    companion object {
        @Volatile
        private var INSTANCE: LocationLocalDataSource? = null

        fun getInstance(locationService: LocationService): LocationLocalDataSource {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: LocationLocalDataSource(locationService).also { INSTANCE = it }
            }
        }
    }

    override fun getCurrentLocation(listener: OnResultListener<Location>) {
        locationService.getCurrentLocation(listener)
    }
}
