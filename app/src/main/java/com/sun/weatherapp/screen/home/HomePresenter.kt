package com.sun.weatherapp.screen.home

import android.location.Location
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.screen.base.BasePresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePresenter(
    private val weatherRepository: WeatherRepository,
    private val locationRepository: LocationRepository
) : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun loadCurrentWeather() {
        getView()?.apply {
            showLoading()
            showSkeletonLoading()
        }
        presenterScope.launch {
            fetchWeatherWithCurrentLocation()
            getView()?.hideLoading()
        }
    }
    
    override fun refreshWeather() {
        getView()?.apply {
            showLoading()
            setRefreshing(true)
            showSkeletonLoading()
        }
        presenterScope.launch {
            fetchWeatherWithCurrentLocation()
            getView()?.hideLoading()
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
                getView()?.hideSkeletonLoading()
                getView()?.setRefreshing(false)
                getView()?.showCurrentWeather(data)
            }

            override fun onError(exception: Exception?) {
                getView()?.hideSkeletonLoading()
                getView()?.setRefreshing(false)
                getView()?.showError(exception?.message ?: "Unknown error occurred")
            }
        })
    }
}
