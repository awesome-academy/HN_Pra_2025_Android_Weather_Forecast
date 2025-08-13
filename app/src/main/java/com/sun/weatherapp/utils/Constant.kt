package com.sun.weatherapp.utils

import com.sun.weatherapp.BuildConfig

object Constant {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5"
    var BASE_API_KEY = "&appid=" + BuildConfig.API_KEY
    const val BASE_LANGUAGE = "&lang=en"
    const val BASE_PAGE = "page=1"
    
    // Weather API endpoints
    const val CURRENT_WEATHER_ENDPOINT = "$BASE_URL/weather"
    
    // Temperature conversion
    const val KELVIN_TO_CELSIUS_OFFSET = 273.15
}

