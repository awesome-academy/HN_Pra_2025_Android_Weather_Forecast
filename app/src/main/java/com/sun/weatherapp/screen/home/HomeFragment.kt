package com.sun.weatherapp.screen.home

import android.Manifest
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.sun.weatherapp.R
import com.sun.weatherapp.databinding.FragmentHomeBinding
import com.sun.weatherapp.screen.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomePresenter>(), HomeContract.View {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initializePresenter() {
        presenter = HomePresenter()
        presenter?.attachView(this)
    }

    override fun setupViews() {

    }

    override fun setupListeners() {
    }

    override fun showCurrentWeather() {
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(message: String) {
    }


}
