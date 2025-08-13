package com.sun.weatherapp

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.LocationService
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.LocationServiceImpl
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource

class WeatherApplication : Application() {

    private val fusedLocationProviderClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val locationService: LocationService by lazy {
        LocationServiceImpl(this, fusedLocationProviderClient)
    }

    val locationRepository: LocationRepository by lazy {
        LocationRepository.getInstance(
            LocationLocalDataSource.getInstance(locationService)
        )
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository.getInstance(
            WeatherRemoteDataSource.getInstance(),
            WeatherLocalDataSource.getInstance()
        )
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
