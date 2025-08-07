package com.sun.weatherapp.utils

import com.sun.weatherapp.BuildConfig

object Constant {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5"
    var BASE_API_KEY = "&api_key=" + BuildConfig.API_KEY
    const val BASE_LANGUAGE = "&language=en-US"
    const val BASE_PAGE = "page=1"
}

