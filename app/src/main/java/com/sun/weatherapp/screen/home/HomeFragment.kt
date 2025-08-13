package com.sun.weatherapp.screen.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.databinding.FragmentHomeBinding
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.utils.toCelsius
import java.util.Locale

class HomeFragment : BaseFragment<FragmentHomeBinding, HomePresenter>(), HomeContract.View {
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun initializePresenter() {
        val app = WeatherApplication.getInstance()
        val locationRepository =  LocationRepository.getInstance(
            LocationLocalDataSource.getInstance(app.locationService)
        )
        val weatherRepository= WeatherRepository.getInstance(
            WeatherRemoteDataSource.getInstance(),
            WeatherLocalDataSource.getInstance()
        )
        presenter = HomePresenter(
            locationRepository,
            weatherRepository
        )
        presenter?.attachView(this)
    }

    override fun setupViews() {
        presenter?.loadCurrentWeather()
    }

    override fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter?.refreshWeather()
        }
    }

    override fun showCurrentWeather(weatherResponse: WeatherResponse) {
        binding.apply {
            currentLocation.text = weatherResponse.name.ifEmpty { "Unknown Location" }
            tvCurrentTemperature.text = "${weatherResponse.main.temp.toCelsius()}°"
            tvFeelsLikeTemperature.text = "Feels like ${weatherResponse.main.feels_like.toCelsius()}°"
            tvWeather.text = weatherResponse.weather.firstOrNull()?.description?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            } ?: "Unknown"
            tvWindSpeed.text = "${weatherResponse.wind.speed} m/s"
            tvPressure.text = "${weatherResponse.main.pressure} hPa"
            tvHumidity.text = "${weatherResponse.main.humidity}%"
            tvUvIndex.text = "N/A"
        }

    }
    
    override fun showSkeletonLoading() {
        binding.apply {
            skeletonLayout.root.visibility = View.VISIBLE
            swipeRefreshLayout.visibility = View.GONE
        }
    }
    
    override fun hideSkeletonLoading() {
        binding.apply {
            skeletonLayout.root.visibility = View.GONE
            swipeRefreshLayout.visibility = View.VISIBLE
        }
    }
    
    override fun setRefreshing(isRefreshing: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isRefreshing
    }

}
