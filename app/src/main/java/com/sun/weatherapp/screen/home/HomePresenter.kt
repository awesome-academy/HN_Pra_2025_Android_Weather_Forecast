package com.sun.weatherapp.screen.home

import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.screen.base.BasePresenter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    private val weatherRepository = WeatherRepository.getInstance(
        WeatherRemoteDataSource.getInstance(),
        WeatherLocalDataSource.getInstance()
    )

    override fun loadCurrentWeather() {
        getView()?.apply {
            showLoading()
            showSkeletonLoading()
        }
        presenterScope.launch {
            fetchWeatherData()
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
            fetchWeatherData()
            getView()?.hideLoading()
        }
    }
    
    private suspend fun fetchWeatherData() {
        delay(1000) // Simulate network delay
        val lat = 21.032508
        val lon = 105.834160

        weatherRepository.getCurrentWeather(lat, lon, object : OnResultListener<WeatherResponse> {
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
