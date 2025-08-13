package com.sun.weatherapp.screen.home

import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.screen.base.BaseContract

interface HomeContract {
    interface View : BaseContract.View {
        fun showCurrentWeather(weatherResponse: WeatherResponse)
        fun showSkeletonLoading()
        fun hideSkeletonLoading()
        fun setRefreshing(isRefreshing: Boolean)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadCurrentWeather()
        fun refreshWeather()
    }
}
