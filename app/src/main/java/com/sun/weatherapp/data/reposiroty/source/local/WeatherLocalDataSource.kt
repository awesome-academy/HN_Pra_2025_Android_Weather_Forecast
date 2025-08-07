package com.sun.weatherapp.data.reposiroty.source.local

import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.source.WeatherDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

class WeatherLocalDataSource : WeatherDataSource.Local {

    override fun getCurrentWeather(listener: OnResultListener<WeatherResponse>) {
        listener.onError(Exception("Local data source not implemented yet"))
    }

    companion object {
        private var instance: WeatherLocalDataSource? = null

        fun getInstance() = synchronized(this) {
            instance ?: WeatherLocalDataSource().also { instance = it }
        }
    }
}