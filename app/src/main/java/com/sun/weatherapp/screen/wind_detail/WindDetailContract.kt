package com.sun.weatherapp.screen.wind_detail

import com.sun.weatherapp.data.model.WindDetailResponse
import com.sun.weatherapp.screen.base.BaseContract

interface WindDetailContract {
    interface View : BaseContract.View {
        fun showWindData(windData: WindDetailResponse)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadWindDetail()
    }
}