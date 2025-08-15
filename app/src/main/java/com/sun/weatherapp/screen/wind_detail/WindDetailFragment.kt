package com.sun.weatherapp.screen.wind_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.databinding.FragmentWindDetailBinding
import com.sun.weatherapp.data.model.WindDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.widget.ChartView
import com.sun.weatherapp.utils.toCelsius
import com.sun.weatherapp.utils.toKmPerHour
import com.sun.weatherapp.utils.toKmPerHourFloat
import com.sun.weatherapp.utils.toWindDirection
import kotlin.random.Random


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

    }

    override fun showWindData(windData: WindDetailResponse) {
        updateUI(windData)
    }


    private fun updateUI(windData: WindDetailResponse) {
        binding.apply {
            tvCurrentTemperature.text = "${windData.current.wind_speed.toKmPerHour()}km/h"
            tvTitle.text = "Hà Nội, Việt Nam"
            val currentWind = windData.current
            val windDirection = currentWind.wind_deg.toWindDirection()
            val windSpeed = currentWind.wind_speed.toKmPerHour()
            val gustSpeed = currentWind.wind_gust?.toKmPerHour() ?: windSpeed
            
            val summaryText = "Gió hiện tại đang thổi với tốc độ ${windSpeed} km/h từ hướng ${windDirection.lowercase()}. " +
                    "Hôm nay, tốc độ gió dao động từ ${windData.daily.minOfOrNull { it.wind_speed.toKmPerHour() } ?: windSpeed} " +
                    "đến ${windData.daily.maxOfOrNull { it.wind_speed.toKmPerHour() } ?: gustSpeed} km/h."
            
            tvSummary.text = summaryText
            setupWindChart(windData)
        }
    }

    private fun setupWindChart(windData: WindDetailResponse) {
        val days = List(windData.daily.take(7).size) { index -> index.toFloat() }
        val windSpeeds = windData.daily.take(7).map { it.wind_speed.toKmPerHourFloat() }

        binding.weatherChart.let { chart ->
            chart.setData(
                days, 
                windSpeeds, 
                maximumNumberOfDisplayPointInXAxis = 7, 
                title = "Dự báo tốc độ gió 7 giờ tới",
                highlightIndex = 0
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
