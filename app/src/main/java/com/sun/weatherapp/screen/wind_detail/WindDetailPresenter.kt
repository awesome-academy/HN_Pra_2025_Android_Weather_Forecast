package com.sun.weatherapp.screen.wind_detail

import android.location.Location
import android.util.Log
import com.sun.weatherapp.data.model.WindDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.screen.base.BasePresenter

class WindDetailPresenter(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
) : BasePresenter<WindDetailContract.View>(),
    WindDetailContract.Presenter {

    override fun loadWindDetail() {
        getView()?.showLoading()

        locationRepository.getCurrentLocation(object : OnResultListener<Location> {
            override fun onSuccess(location: Location) {
                fetchWeatherDataWithLocation(location.latitude, location.longitude)
            }

            override fun onError(exception: Exception?) {
                getView()?.showError(exception?.message ?: "Failed to get current location")
            }
        })


    }

    private fun fetchWeatherDataWithLocation(latitude: Double, longitude: Double) {
        weatherRepository.getWindDetail(latitude, longitude, object : OnResultListener<WindDetailResponse> {
            override fun onSuccess(data: WindDetailResponse) {
                Log.d("WindDetailPresenter", "Wind detail data fetched successfully: ${data}")
                getView()?.hideLoading()
                getView()?.showWindData(data)
            }


            override fun onError(exception: java.lang.Exception?) {
                Log.e("WindDetailPresenter", "Error fetching wind detail data", exception)
                getView()?.hideLoading()
                getView()?.showError(exception?.message ?: "Unknown error")
            }
        })
    }
}