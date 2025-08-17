package com.sun.weatherapp.screen.rain_detail

import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.screen.base.BaseContract

interface RainDetailContract {
    interface View : BaseContract.View {
        fun showRainData(rainData: WeatherDetailResponse)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadRainDetail()
        fun refreshData()
    }
}
