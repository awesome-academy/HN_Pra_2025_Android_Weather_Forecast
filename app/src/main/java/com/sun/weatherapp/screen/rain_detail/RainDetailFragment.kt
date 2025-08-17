package com.sun.weatherapp.screen.rain_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.databinding.FragmentRainDetailBinding
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.rain_detail.adapter.RainChanceAdapter
import com.sun.weatherapp.screen.widget.ChartView
import com.sun.weatherapp.utils.WeatherIconLoader
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class RainDetailFragment : BaseFragment<FragmentRainDetailBinding, RainDetailPresenter>(), RainDetailContract.View {
    
    private lateinit var rainChanceAdapter: RainChanceAdapter
    
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRainDetailBinding {
        return FragmentRainDetailBinding.inflate(
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
        presenter = RainDetailPresenter(
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
            rainChanceAdapter = RainChanceAdapter()
            listViewRainChance.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = rainChanceAdapter
                isNestedScrollingEnabled = false // Disable RecyclerView scroll
            }
        }
        presenter?.loadRainDetail()
    }

    override fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter?.refreshData()
        }
    }

    override fun showRainData(rainData: WeatherDetailResponse) {
        updateUI(rainData)
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

    private fun updateUI(rainData: WeatherDetailResponse) {
        binding.apply {
            val currentRainChance = rainData.hourly.firstOrNull()?.let {
                (it.pop * 100).roundToInt()
            }
            
            tvCurrentRainChance.text = "${currentRainChance}%"
            tvTitle.text = "Hà Nội, Việt Nam"
            
            val currentWeather = rainData.current
            
            // Load weather icon từ API
            if (currentWeather.weather.isNotEmpty()) {
                val iconCode = currentWeather.weather[0].icon
                WeatherIconLoader.loadWeatherIcon(iconCode, icWeatherIcon)
            }
            
            // Set data cho ListView
            rainChanceAdapter.updateData(rainData.hourly)
            
            // Setup rain probability chart
            setupRainChart(rainData)
            
            // Update summary text
            updateSummaryText(rainData)
        }
    }

    private fun setupRainChart(rainData: WeatherDetailResponse) {
        val hours = rainData.hourly.take(24).mapIndexed { index, _ -> 
            index.toFloat() + 1f 
        }
        val rainProbabilities = rainData.hourly.take(24).map { 
            (it.pop * 100).toFloat()
        }

        binding.weatherChart.let { chart ->
            chart.setData(
                hours, 
                rainProbabilities, 
                maximumNumberOfDisplayPointInXAxis = 12, 
                title = "Khả năng mưa 24 giờ tới (%)",
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
    
    private fun updateSummaryText(rainData: WeatherDetailResponse) {
        val currentRainChance = (rainData.hourly.firstOrNull()?.pop ?: 0.0 * 100).roundToInt()
        
        val next12Hours = rainData.hourly.take(12)
        val maxRainProbability = next12Hours.maxOfOrNull { it.pop } ?: 0.0
        val maxRainHour = next12Hours.indexOfFirst { it.pop == maxRainProbability }
        
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = next12Hours[maxRainHour].dt * 1000
        val timeFormat = SimpleDateFormat("h a", Locale.getDefault())
        val maxRainTime = timeFormat.format(calendar.time)
        
        val summaryText = if (currentRainChance > 0) {
            "Khả năng mưa hiện tại là ${currentRainChance}%. " +
                    "Hôm nay có thể có mưa với khả năng cao nhất vào lúc $maxRainTime (${(maxRainProbability * 100).roundToInt()}%)."
        } else {
            "Hiện tại không có khả năng mưa. " +
                    "Khả năng mưa cao nhất trong ngày là ${(maxRainProbability * 100).roundToInt()}% vào lúc $maxRainTime."
        }
        
        binding.tvSummary.text = summaryText
    }
}
