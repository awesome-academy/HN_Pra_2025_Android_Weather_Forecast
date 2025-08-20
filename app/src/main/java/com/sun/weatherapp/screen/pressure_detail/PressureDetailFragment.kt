package com.sun.weatherapp.screen.pressure_detail

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weatherapp.R
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.databinding.FragmentPressureDetailBinding
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.pressure_detail.adapter.HourlyPressureAdapter
import com.sun.weatherapp.screen.widget.ChartView
import com.sun.weatherapp.utils.WeatherIconLoader

class PressureDetailFragment : BaseFragment<FragmentPressureDetailBinding, PressureDetailPresenter>(), PressureDetailContract.View {
    
    private lateinit var hourlyPressureAdapter: HourlyPressureAdapter
    
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPressureDetailBinding {
        return FragmentPressureDetailBinding.inflate(
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
        presenter = PressureDetailPresenter(
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
            hourlyPressureAdapter = HourlyPressureAdapter()
            recyclerViewPressureDetail.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = hourlyPressureAdapter
            }
            icSearch.setOnClickListener {
                findNavController().navigate(R.id.action_pressure_details_fragment_to_search_fragment)
            }
        }
        presenter?.loadPressureDetail()
    }

    override fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter?.refreshData()
        }
    }

    override fun showPressureData(pressureData: WeatherDetailResponse) {
        updateUI(pressureData)
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

    private fun updateUI(pressureData: WeatherDetailResponse) {
        binding.apply {
            // Hiển thị áp suất hiện tại
            tvCurrentPressure.text = "${pressureData.current.pressure}hPa"
            tvTitle.text = "Hà Nội, Việt Nam"
            
            val currentWeather = pressureData.current
            
            // Load weather icon từ API
            if (currentWeather.weather.isNotEmpty()) {
                val iconCode = currentWeather.weather[0].icon
                WeatherIconLoader.loadWeatherIcon(iconCode, icWeatherIcon)
            }
            
            // Update hourly pressure RecyclerView
            hourlyPressureAdapter.updateData(pressureData.hourly)
            
            // Tính toán min/max pressure trong ngày
            val minPressure = pressureData.hourly.take(24).minOfOrNull { it.pressure } ?: currentWeather.pressure
            val maxPressure = pressureData.hourly.take(24).maxOfOrNull { it.pressure } ?: currentWeather.pressure
            
            val pressureStatus = getPressureStatus(currentWeather.pressure)
            val summaryText = "Áp suất khí quyển hiện tại là ${currentWeather.pressure} hPa, ở mức $pressureStatus. " +
                    "Hôm nay áp suất dao động từ ${minPressure} đến ${maxPressure} hPa."
            
            tvSummary.text = summaryText
            setupPressureChart(pressureData)
        }
    }

    private fun setupPressureChart(pressureData: WeatherDetailResponse) {
        val hours = List(pressureData.hourly.take(24).size) { index -> index.toFloat() + 1f }
        val pressures = pressureData.hourly.take(24).map { it.pressure.toFloat() }

        binding.weatherChart.let { chart ->
            chart.setData(
                hours, 
                pressures, 
                maximumNumberOfDisplayPointInXAxis = 12, 
                title = "Dự báo áp suất 24 giờ tới",
                highlightIndex = 0,
                xT = "Giờ"
            )
            chart.setOnPointSelectedListener(object : ChartView.OnPointSelectedListener {
                override fun onPointSelected(xValue: Float, yValue: Float, index: Int) {
                    // Handle point selection if needed
                }
                override fun onPointDeselected() {
                    // Handle point deselection if needed
                }
            })
        }
    }
    
    private fun getPressureStatus(pressure: Int): String {
        return when {
            pressure < 1000 -> "thấp"
            pressure > 1020 -> "cao"
            else -> "bình thường"
        }
    }
}
