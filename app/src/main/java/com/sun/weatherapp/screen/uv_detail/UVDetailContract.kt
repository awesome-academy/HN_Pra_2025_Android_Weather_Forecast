package com.sun.weatherapp.screen.uv_detail

import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.screen.base.BaseContract

interface UVDetailContract {
    interface View : BaseContract.View {
        fun showUVData(data: WeatherDetailResponse)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun funLoadUVDetail()
        fun refreshData()
    }
}