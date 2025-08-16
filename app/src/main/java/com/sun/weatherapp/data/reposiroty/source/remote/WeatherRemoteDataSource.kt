package com.sun.weatherapp.data.reposiroty.source.remote

import com.sun.mvp.data.repository.source.remote.fetchjson.GetJsonFromUrl
import com.sun.weatherapp.data.model.WeatherEntry
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.source.WeatherDataSource
import com.sun.weatherapp.utils.Constant

class WeatherRemoteDataSource : WeatherDataSource.Remote {

    companion object {
        private var instance: WeatherRemoteDataSource? = null

        fun getInstance() = synchronized(this) {
            instance ?: WeatherRemoteDataSource().also { instance = it }
        }
    }

    override fun getCurrentWeather(lat: Double, lon: Double, listener: OnResultListener<WeatherResponse>) {
        val url = "${Constant.CURRENT_WEATHER_ENDPOINT}?lat=$lat&lon=$lon"
        GetJsonFromUrl(
            urlString = url,
            keyEntity = WeatherEntry.WEATHER,
            listener = listener
        )
    }

    override fun getWeatherDetail(lat: Double, lon: Double, listener: OnResultListener<WeatherDetailResponse>) {
        val url = "${Constant.ONECALL_WEATHER_ENDPOINT}?lat=$lat&lon=$lon&exclude=minutely"
        GetJsonFromUrl(
            urlString = url,
            keyEntity = WeatherEntry.WEATHER_DETAIL,
            listener = listener
        )
    }
}
