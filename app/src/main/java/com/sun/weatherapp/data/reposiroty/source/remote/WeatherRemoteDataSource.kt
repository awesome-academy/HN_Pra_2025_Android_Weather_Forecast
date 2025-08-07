package com.sun.weatherapp.data.reposiroty.source.remote

import com.sun.mvp.data.repository.source.remote.fetchjson.GetJsonFromUrl
import com.sun.weatherapp.data.model.WeatherEntry
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.source.WeatherDataSource

class WeatherRemoteDataSource : WeatherDataSource.Remote {

    companion object {
        private var instance: WeatherRemoteDataSource? = null

        fun getInstance() = synchronized(this) {
            instance ?: WeatherRemoteDataSource().also { instance = it }
        }
    }

    override fun getCurrentWeather(listener: OnResultListener<WeatherResponse>) {
        GetJsonFromUrl(
            urlString = "https://api.openweathermap.org/data/2.5/weather",
            keyEntity = WeatherEntry.WEATHER,
            listener = listener
        )
    }
}
