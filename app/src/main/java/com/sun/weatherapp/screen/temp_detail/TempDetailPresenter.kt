package com.sun.weatherapp.screen.temp_detail

import android.location.Location
import android.util.Log
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener
import com.sun.weatherapp.screen.base.BasePresenter

class TempDetailPresenter(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository,
) : BasePresenter<TempDetailContract.View>(),
    TempDetailContract.Presenter {

    override fun funLoadTempDetail() {
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
        funLoadTempDetail()
    }

    private fun fetchWeatherDataWithLocation(latitude: Double, longitude: Double) {
        weatherRepository.getWeatherDetail(latitude, longitude, object :
            OnResultListener<WeatherDetailResponse> {
            override fun onSuccess(data: WeatherDetailResponse) {
                Log.d("TempDetailPresenter", "Temperature detail data fetched successfully: ${data}")
                getView()?.hideLoading()
                getView()?.showTempData(data)
            }


            override fun onError(exception: java.lang.Exception?) {
                getView()?.hideLoading()
                getView()?.showError(exception?.message ?: "Unknown error")
            }
        })
    }
}