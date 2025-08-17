package com.sun.weatherapp.screen.rain_detail

import android.location.Location
import android.util.Log
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.screen.base.BasePresenter

class RainDetailPresenter(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
) : BasePresenter<RainDetailContract.View>(),
    RainDetailContract.Presenter {

    override fun loadRainDetail() {
        getView()?.showLoading()

        locationRepository.getCurrentLocation(object : OnResultListener<Location> {
            override fun onSuccess(location: Location) {
                fetchWeatherDataWithLocation(location.latitude, location.longitude)
            }

            override fun onError(exception: Exception?) {
                getView()?.hideLoading()
                getView()?.showError(exception?.message ?: "Failed to get current location")
            }
        })
    }

    override fun refreshData() {
        loadRainDetail()
    }

    private fun fetchWeatherDataWithLocation(latitude: Double, longitude: Double) {
        weatherRepository.getWeatherDetail(latitude, longitude, object : OnResultListener<WeatherDetailResponse> {
            override fun onSuccess(data: WeatherDetailResponse) {
                Log.d("RainDetailPresenter", "Rain detail data fetched successfully: ${data}")
                getView()?.hideLoading()
                getView()?.showRainData(data)
            }

            override fun onError(exception: java.lang.Exception?) {
                Log.e("RainDetailPresenter", "Error fetching rain detail data", exception)
                getView()?.hideLoading()
                getView()?.showError(exception?.message ?: "Unknown error")
            }
        })
    }
}
