package com.sun.weatherapp.data.reposiroty.source

import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

interface WeatherDataSource {
    /**
     * Local
     */
    interface Local {
        fun getCurrentWeather(lat: Double, lon: Double, listener: OnResultListener<WeatherResponse>)
    }

    /**
     * Remote
     */
    interface Remote {
        fun getCurrentWeather(lat: Double, lon: Double, listener: OnResultListener<WeatherResponse>)
    }
}
