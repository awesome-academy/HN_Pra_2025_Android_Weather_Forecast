package com.sun.weatherapp.screen.wind_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.databinding.FragmentTempDetailBinding
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.temp_detail.TempDetailContract
import com.sun.weatherapp.screen.temp_detail.TempDetailPresenter
import com.sun.weatherapp.screen.temp_detail.adapter.HourlyWeatherAdapter
import com.sun.weatherapp.utils.WeatherIconLoader
import com.sun.weatherapp.utils.toCelsius


class TempDetailFragment : BaseFragment<FragmentTempDetailBinding, TempDetailPresenter>(), TempDetailContract.View {

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTempDetailBinding {
        return FragmentTempDetailBinding.inflate(
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
        presenter = TempDetailPresenter(
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
            
            // Setup RecyclerView
            hourlyWeatherAdapter = HourlyWeatherAdapter()
            recyclerViewWindDetail.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = hourlyWeatherAdapter
            }
        }
        presenter?.funLoadTempDetail()
    }

    override fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter?.refreshData()
        }
    }

    override fun showTempData(data: WeatherDetailResponse) {
        updateUI(data)
    }

    override fun showLoading() {
        if (binding.swipeRefreshLayout.isRefreshing) {
            return
        }
        super.showLoading()
    }

    override fun hideLoading() {
        super.hideLoading()
        binding.swipeRefreshLayout.isRefreshing = false
    }


    private fun updateUI(data: WeatherDetailResponse) {
        binding.apply {
            // Update header information
            tvCurrentTemperature.text = "${data.current.temp.toCelsius()}°"
            tvFeelsLikeTemperature.text = "Cảm giác như ${data.current.feels_like.toCelsius()}°"
            
            // Load weather icon for current weather
            if (data.current.weather.isNotEmpty()) {
                val iconCode = data.current.weather[0].icon
                WeatherIconLoader.loadWeatherIcon(iconCode, icWeatherIcon)
            }
            
            // Update hourly weather RecyclerView
            hourlyWeatherAdapter.updateData(data.hourly)
            
            // Setup temperature chart
            setupTempChart(data)
            
            // Update summary text
            updateSummaryText(data)
        }
    }

    private fun setupTempChart(data: WeatherDetailResponse) {
        // Prepare data for temperature chart using daily data
        val dailyData = data.daily.take(7) // Take first 7 days
        
        val xValues = dailyData.mapIndexed { index, _ -> 
            index.toFloat() + 1 // Day 1, 2, 3, etc.
        }
        
        val yValues = dailyData.map { daily ->
            daily.temp.day.toCelsius().toFloat() // Day temperature
        }
        
        // Set chart data
        binding.weatherChart.setData(
            xValues = xValues,
            yValues = yValues,
            highlightIndex = 0, // Highlight today
            maximumNumberOfDisplayPointInXAxis = 7,
            title = "Nhiệt độ 7 ngày tới",
            xT = "Ngày"
        )
    }
    
    private fun updateSummaryText(data: WeatherDetailResponse) {
        val todayWeather = data.daily.firstOrNull()
        todayWeather?.let { today ->
            val minTemp = today.temp.min.toCelsius()
            val maxTemp = today.temp.max.toCelsius()
            val description = if (today.weather.isNotEmpty()) {
                today.weather[0].description
            } else {
                "thời tiết bình thường"
            }
            
            val summaryText = "Hôm nay nhiệt độ dao động từ ${minTemp}°C đến ${maxTemp}°C với ${description}. " +
                    "Độ ẩm hiện tại là ${data.current.humidity}% và tốc độ gió ${data.current.wind_speed} m/s."
            
            binding.tvSummary.text = summaryText
        }
    }
}
