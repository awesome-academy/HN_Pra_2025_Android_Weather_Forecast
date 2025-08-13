package com.sun.weatherapp.data.reposiroty

import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.source.WeatherDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

class WeatherRepository private constructor(
    private val remote: WeatherDataSource.Remote,
    private val local: WeatherDataSource.Local
) : WeatherDataSource.Local, WeatherDataSource.Remote {

    override fun getCurrentWeather(lat: Double, lon: Double, listener: OnResultListener<WeatherResponse>) {
        remote.getCurrentWeather(lat, lon, listener)
    }

    companion object {
        private var instance: WeatherRepository? = null

        fun getInstance(remote: WeatherDataSource.Remote, local: WeatherDataSource.Local) = synchronized(this) {
            instance ?: WeatherRepository(remote, local).also { instance = it }
        }
    }
}
