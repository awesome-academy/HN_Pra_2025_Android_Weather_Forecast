package com.sun.weatherapp.screen.home

import com.sun.weatherapp.screen.base.BasePresenter
import kotlinx.coroutines.launch

class HomePresenter() : BasePresenter<HomeContract.View>(), HomeContract.Presenter {

    override fun loadCurrentWeather() {
        presenterScope.launch {

        }
    }
}
