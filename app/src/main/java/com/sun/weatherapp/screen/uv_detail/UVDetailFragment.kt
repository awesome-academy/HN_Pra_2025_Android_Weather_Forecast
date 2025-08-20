package com.sun.weatherapp.screen.uv_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.weatherapp.R
import com.sun.weatherapp.WeatherApplication
import com.sun.weatherapp.data.model.WeatherDetailResponse
import com.sun.weatherapp.data.reposiroty.LocationRepository
import com.sun.weatherapp.data.reposiroty.WeatherRepository
import com.sun.weatherapp.data.reposiroty.source.local.LocationLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.local.WeatherLocalDataSource
import com.sun.weatherapp.data.reposiroty.source.remote.WeatherRemoteDataSource
import com.sun.weatherapp.databinding.FragmentUvDetailBinding
import com.sun.weatherapp.screen.base.BaseFragment
import com.sun.weatherapp.screen.uv_detail.adapter.HourlyUVAdapter
import com.sun.weatherapp.utils.WeatherIconLoader
import kotlin.math.roundToInt

class UVDetailFragment : BaseFragment<FragmentUvDetailBinding, UVDetailPresenter>(), UVDetailContract.View {

    private lateinit var hourlyUVAdapter: HourlyUVAdapter

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentUvDetailBinding {
        return FragmentUvDetailBinding.inflate(
            layoutInflater,
            container,
            false
        )
    }

    override fun initializePresenter() {
        val app = WeatherApplication.getInstance()
        val locationRepository = LocationRepository.getInstance(
            LocationLocalDataSource.getInstance(app.locationService)
        )
        val weatherRepository = WeatherRepository.getInstance(
            WeatherRemoteDataSource.getInstance(),
            WeatherLocalDataSource.getInstance()
        )
        presenter = UVDetailPresenter(
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
            hourlyUVAdapter = HourlyUVAdapter()
            recyclerViewUVDetail.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                adapter = hourlyUVAdapter
                isNestedScrollingEnabled = false
            }
            icSearch.setOnClickListener {
                findNavController().navigate(R.id.action_uv_details_fragment_to_search_fragment)
            }
        }
        presenter?.funLoadUVDetail()
    }

    override fun setupListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            presenter?.refreshData()
        }
    }

    override fun showUVData(data: WeatherDetailResponse) {
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
            val currentUV = data.current.uvi.roundToInt()
            tvCurrentUV.text = currentUV.toString()
            tvUVLevel.text = getUVLevel(currentUV)
            
            // Load weather icon for current weather
            if (data.current.weather.isNotEmpty()) {
                val iconCode = data.current.weather[0].icon
                WeatherIconLoader.loadWeatherIcon(iconCode, icWeatherIcon)
            }
            
            // Update hourly UV RecyclerView
            hourlyUVAdapter.updateData(data.hourly)
            
            // Setup UV chart
            setupUVChart(data)
            
            // Update summary text
            updateSummaryText(data)
        }
    }
    
    private fun setupUVChart(data: WeatherDetailResponse) {
        // Prepare data for UV chart using hourly data
        val hourlyData = data.hourly.take(24) // Take first 24 hours
        
        val xValues = hourlyData.mapIndexed { index, _ -> 
            index.toFloat() + 1 // Hour 1, 2, 3, etc.
        }
        
        val yValues = hourlyData.map { hourly ->
            hourly.uvi.toFloat() // UV index
        }
        
        // Set chart data
        binding.weatherChart.setData(
            xValues = xValues,
            yValues = yValues,
            highlightIndex = 0, // Highlight current hour
            maximumNumberOfDisplayPointInXAxis = 12,
            title = "Chỉ số UV 24 giờ tới",
            xT = "Giờ"
        )
    }
    
    private fun updateSummaryText(data: WeatherDetailResponse) {
        val currentUV = data.current.uvi.roundToInt()
        val uvLevel = getUVLevel(currentUV)
        
        // Find min/max UV in next 24 hours
        val next24Hours = data.hourly.take(24)
        val minUV = next24Hours.minOfOrNull { it.uvi }?.roundToInt() ?: 0
        val maxUV = next24Hours.maxOfOrNull { it.uvi }?.roundToInt() ?: currentUV
        
        val advice = getUVAdvice(currentUV)
        
        val summaryText = "Chỉ số UV hiện tại là $currentUV, ở mức $uvLevel. " +
                "Hôm nay chỉ số UV dao động từ $minUV đến $maxUV. $advice"
        
        binding.tvSummary.text = summaryText
    }
    
    private fun getUVLevel(uvIndex: Int): String {
        return when (uvIndex) {
            0, 1, 2 -> "thấp"
            3, 4, 5 -> "trung bình"
            6, 7 -> "cao"
            8, 9, 10 -> "rất cao"
            else -> "cực cao"
        }
    }
    
    private fun getUVAdvice(uvIndex: Int): String {
        return when (uvIndex) {
            0, 1, 2 -> "Có thể ra ngoài mà không cần bảo vệ đặc biệt."
            3, 4, 5 -> "Nên sử dụng kem chống nắng khi ra ngoài."
            6, 7 -> "Cần sử dụng kem chống nắng và mặc quần áo bảo vệ."
            8, 9, 10 -> "Tránh ra ngoài vào giữa trưa, sử dụng kem chống nắng SPF cao."
            else -> "Rất nguy hiểm, hạn chế ra ngoài và bảo vệ toàn thân."
        }
    }
}