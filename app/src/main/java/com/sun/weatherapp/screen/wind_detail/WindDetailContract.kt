package com.sun.weatherapp.screen.wind_detail

import com.sun.weatherapp.screen.base.BaseContract

interface WindDetailContract {
    interface View : BaseContract.View {

    }

    interface Presenter : BaseContract.Presenter<View> {
    }
}