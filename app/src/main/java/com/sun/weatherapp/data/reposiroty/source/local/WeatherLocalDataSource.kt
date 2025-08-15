package com.sun.weatherapp.data.reposiroty.source.local

import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.model.WindDetailResponse
import com.sun.weatherapp.data.reposiroty.source.WeatherDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

class WeatherLocalDataSource : WeatherDataSource.Local {

    override fun getCurrentWeather(lat: Double, lon: Double, listener: OnResultListener<WeatherResponse>) {
        listener.onError(Exception("Local data source not implemented yet"))
    }

    override fun getWindDetail(lat: Double, lon: Double, listener: OnResultListener<WindDetailResponse>) {
        listener.onError(Exception("Local wind detail data source not implemented yet"))
    }

    companion object {
        private var instance: WeatherLocalDataSource? = null

        fun getInstance() = synchronized(this) {
            instance ?: WeatherLocalDataSource().also { instance = it }
        }
    }
}
