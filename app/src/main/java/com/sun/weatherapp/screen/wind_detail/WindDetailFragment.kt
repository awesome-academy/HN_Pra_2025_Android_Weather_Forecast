package com.sun.weatherapp.screen.wind_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.databinding.FragmentWindDetailBinding
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.widget.ChartView
import com.sun.weatherapp.utils.toKmPerHour
import com.sun.weatherapp.utils.toKmPerHourFloat
import com.sun.weatherapp.utils.toWindDirection
import com.sun.weatherapp.utils.WeatherIconLoader


class WindDetailFragment : BaseFragment<FragmentWindDetailBinding, WindDetailPresenter>(), WindDetailContract.View {
    
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentWindDetailBinding {
        return FragmentWindDetailBinding.inflate(
            layoutInflater,
            container,
            false
        )
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
        presenter = WindDetailPresenter(
            locationRepository,
            weatherRepository
        )
        presenter?.attachView(this)
    }

    override fun setupViews() {
        binding.apply {
            icBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
        presenter?.loadWindDetail()
    }

    override fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter?.refreshData()
        }
    }

    override fun showWindData(windData: WeatherDetailResponse) {
        updateUI(windData)
    }

    override fun showLoading() {
        if (binding.swipeRefreshLayout.isRefreshing) {
            return
        }
        super.showLoading()
    }

    override fun hideLoading() {
        // Hide cả dialog và SwipeRefreshLayout
        super.hideLoading()
        binding.swipeRefreshLayout.isRefreshing = false
    }


    private fun updateUI(windData: WeatherDetailResponse) {
        binding.apply {
            // Hiển thị tốc độ gió hiện tại (convert từ m/s sang km/h)
            tvCurrentTemperature.text = "${windData.current.wind_speed.toKmPerHour()}km/h"
            tvTitle.text = "Hà Nội, Việt Nam"
            
            val currentWind = windData.current
            val windDirection = currentWind.wind_deg.toWindDirection()
            val windSpeed = currentWind.wind_speed.toKmPerHour()
            val gustSpeed = currentWind.wind_gust?.toKmPerHour() ?: windSpeed
            
            // Load weather icon từ API
            if (currentWind.weather.isNotEmpty()) {
                val iconCode = currentWind.weather[0].icon
                WeatherIconLoader.loadWeatherIcon(iconCode, icWeatherIcon)
            }
            
            // Tính toán min/max wind speed trong ngày (convert từ m/s sang km/h)
            val minWindSpeed = windData.hourly.take(7).minOfOrNull { it.wind_speed.toKmPerHour() } ?: windSpeed
            val maxWindSpeed = windData.hourly.take(7).maxOfOrNull { it.wind_speed.toKmPerHour() } ?: gustSpeed
            
            val summaryText = "Gió hiện tại đang thổi với tốc độ ${windSpeed} km/h từ hướng ${windDirection.lowercase()}. " +
                    "Hôm nay, tốc độ gió dao động từ ${minWindSpeed} đến ${maxWindSpeed} km/h."
            
            tvSummary.text = summaryText
            setupWindChart(windData)
        }
    }

    private fun setupWindChart(windData: WeatherDetailResponse) {
        val days = List(windData.hourly.take(7).size) { index -> index.toFloat() + 1f }
        val windSpeeds = windData.hourly.take(7).map { it.wind_speed.toKmPerHourFloat() }

        binding.weatherChart.let { chart ->
            chart.setData(
                days, 
                windSpeeds, 
                maximumNumberOfDisplayPointInXAxis = 7, 
                title = "Dự báo tốc độ gió 7 giờ tới",
                highlightIndex = 0,
                xT = "Giờ tới"
            )
            chart.setOnPointSelectedListener(object : ChartView.OnPointSelectedListener {
                override fun onPointSelected(xValue: Float, yValue: Float, index: Int) {
                }
                override fun onPointDeselected() {
                }
            })
        }
    }
}
