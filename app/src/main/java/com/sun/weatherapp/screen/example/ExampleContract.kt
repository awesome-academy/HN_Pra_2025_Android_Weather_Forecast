package com.sun.weatherapp.screen.example

import com.sun.weatherapp.screen.base.BaseContract

interface ExampleContract : BaseContract<ExampleContract.View, ExampleContract.Presenter> {
    interface View : BaseContract.View {
        fun showMyTeams(teams: List<String>)
    }

    interface Presenter : BaseContract.Presenter<View> {
        fun loadMyTeams()
    }
}
