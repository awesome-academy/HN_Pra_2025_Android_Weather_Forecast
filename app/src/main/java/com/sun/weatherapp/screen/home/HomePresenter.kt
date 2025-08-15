package com.sun.weatherapp.screen.home

import android.location.Location
import android.util.Log
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.LocationService
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.screen.base.BasePresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePresenter(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
) : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun loadCurrentWeather() {
        getView()?.showSkeletonLoading()
        presenterScope.launch {
            fetchWeatherWithCurrentLocation()
        }
    }
    
    override fun refreshWeather() {
        getView()?.apply {
            setRefreshing(true)
            showSkeletonLoading()
        }
        presenterScope.launch {
            fetchWeatherWithCurrentLocation()
        }
    }
    
    private suspend fun fetchWeatherWithCurrentLocation() {
        delay(1000)
        
        locationRepository.getCurrentLocation(object : OnResultListener<Location> {
            override fun onSuccess(location: Location) {
                fetchWeatherDataWithLocation(location.latitude, location.longitude)
            }

            override fun onError(exception: Exception?) {
                getView()?.hideSkeletonLoading()
                getView()?.setRefreshing(false)
                getView()?.showError(exception?.message ?: "Failed to get current location")
            }
        })
    }
    
    private fun fetchWeatherDataWithLocation(latitude: Double, longitude: Double) {
        weatherRepository.getCurrentWeather(latitude, longitude, object : OnResultListener<WeatherResponse> {
            override fun onSuccess(data: WeatherResponse) {
                Log.d("HomePresenter", "Weather data fetched successfully: ${data}")
                getView()?.hideSkeletonLoading()
                getView()?.setRefreshing(false)
                getView()?.showCurrentWeather(data)
            }

            override fun onError(exception: Exception?) {
                Log.e("HomePresenter", "Error fetching weather data: ${exception?.message}")
                getView()?.hideSkeletonLoading()
                getView()?.setRefreshing(false)
                getView()?.showError(exception?.message ?: "Unknown error occurred")
            }
        })
    }
}
