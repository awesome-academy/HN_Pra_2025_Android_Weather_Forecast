package com.sun.weatherapp.screen.pressure_detail

import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.screen.base.BaseContract

interface PressureDetailContract {
    interface View : BaseContract.View {
        fun showPressureData(pressureData: WeatherDetailResponse)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadPressureDetail()
        fun refreshData()
    }
}
