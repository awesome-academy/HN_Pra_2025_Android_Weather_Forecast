package com.sun.weatherapp.screen.uv_detail

import android.location.Location
import android.util.Log
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.screen.base.BasePresenter

class UVDetailPresenter(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
) : BasePresenter<UVDetailContract.View>(),
    UVDetailContract.Presenter {

    override fun funLoadUVDetail() {
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
        funLoadUVDetail()
    }

    private fun fetchWeatherDataWithLocation(latitude: Double, longitude: Double) {
        weatherRepository.getWeatherDetail(latitude, longitude, object :
            OnResultListener<WeatherDetailResponse> {
            override fun onSuccess(data: WeatherDetailResponse) {
                Log.d("UVDetailPresenter", "UV detail data fetched successfully: ${data}")
                getView()?.hideLoading()
                getView()?.showUVData(data)
            }

            override fun onError(exception: java.lang.Exception?) {
                getView()?.hideLoading()
                getView()?.showError(exception?.message ?: "Unknown error")
            }
        })
    }
}