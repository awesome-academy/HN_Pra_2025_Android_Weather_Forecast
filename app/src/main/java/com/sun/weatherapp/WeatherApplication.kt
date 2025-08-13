package com.sun.weatherapp

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sun.weatherapp.data.reposiroty.source.LocationService
import com.sun.weatherapp.data.reposiroty.source.local.LocationServiceImpl

class WeatherApplication : Application() {

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    val locationService: LocationService by lazy {
        LocationServiceImpl(this, fusedLocationProviderClient)
    }

    companion object {
        @Volatile
        private var INSTANCE: WeatherApplication? = null

        fun getInstance(): WeatherApplication {
            return INSTANCE ?: throw IllegalStateException("Application not initialized")
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}
