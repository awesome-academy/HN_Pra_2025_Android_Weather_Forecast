package com.sun.weatherapp.screen.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.databinding.FragmentHomeBinding
import com.sun.weatherapp.screen.base.BaseFragment
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
        presenter = HomePresenter(
            weatherRepository = app.weatherRepository,
            locationRepository = app.locationRepository
        )
        presenter?.attachView(this)
    }

    override fun setupViews() {
        // Load weather data when fragment is created
        presenter?.loadCurrentWeather()
    }

    override fun setupListeners() {
        // Setup pull-to-refresh
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter?.refreshWeather()
        }
    }

    override fun showCurrentWeather(weatherResponse: WeatherResponse) {
        // Update UI with weather data
        binding.apply {
            currentLocation.text = weatherResponse.name.ifEmpty { "Unknown Location" }
            tvCurrentTemperature.text = "${(weatherResponse.main.temp - 273.15).toInt()}°"
            tvFeelsLikeTemperature.text = "Feels like ${(weatherResponse.main.feels_like - 273.15).toInt()}°"
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

        Log.d("HomeFragment", "Weather data loaded: $weatherResponse")
    }
    
    override fun showSkeletonLoading() {
        binding.apply {
            skeletonLayout.root.visibility = View.VISIBLE
            swipeRefreshLayout.visibility = View.GONE
        }
        Log.d("HomeFragment", "Showing skeleton loading...")
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
