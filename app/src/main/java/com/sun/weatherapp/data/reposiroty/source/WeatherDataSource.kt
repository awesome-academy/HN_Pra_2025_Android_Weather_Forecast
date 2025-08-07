package com.sun.weatherapp.data.reposiroty.source

import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

interface WeatherDataSource {
    /**
     * Local
     */
    interface Local {
        fun getCurrentWeather(listener: OnResultListener<WeatherResponse>)
    }

    /**
     * Remote
     */
    interface Remote {
        fun getCurrentWeather(listener: OnResultListener<WeatherResponse>)
    }
}
