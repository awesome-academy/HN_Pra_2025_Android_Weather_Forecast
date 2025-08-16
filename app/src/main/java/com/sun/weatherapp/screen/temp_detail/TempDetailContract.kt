package com.sun.weatherapp.screen.temp_detail

import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.screen.base.BaseContract

interface TempDetailContract {
    interface View : BaseContract.View {
        fun showTempData(data: WeatherDetailResponse)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun funLoadTempDetail()
        fun refreshData()
    }
}