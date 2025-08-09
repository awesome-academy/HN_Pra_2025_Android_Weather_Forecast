package com.sun.weatherapp.screen.home

import com.sun.weatherapp.screen.base.BaseContract

interface HomeContract {
    interface View : BaseContract.View {
        fun showCurrentWeather()
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadCurrentWeather()
    }
}