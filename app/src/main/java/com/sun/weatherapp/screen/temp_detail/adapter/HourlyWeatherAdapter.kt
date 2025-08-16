package com.sun.weatherapp.screen.temp_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.weatherapp.data.model.HourlyWeather
import com.sun.weatherapp.databinding.ItemPredictTempBinding
import com.sun.weatherapp.utils.WeatherIconLoader
import com.sun.weatherapp.utils.toCelsius
import java.text.SimpleDateFormat
import java.util.*

class HourlyWeatherAdapter(
    private var hourlyWeatherList: List<HourlyWeather> = emptyList()
) : RecyclerView.Adapter<HourlyWeatherAdapter.HourlyWeatherViewHolder>() {

    fun updateData(newHourlyWeatherList: List<HourlyWeather>) {
        hourlyWeatherList = newHourlyWeatherList.take(24) // Chỉ lấy 24 giờ đầu
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherViewHolder {
        val binding = ItemPredictTempBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourlyWeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyWeatherViewHolder, position: Int) {
        holder.bind(hourlyWeatherList[position], position)
    }

    override fun getItemCount(): Int = hourlyWeatherList.size

    inner class HourlyWeatherViewHolder(
        private val binding: ItemPredictTempBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hourlyWeather: HourlyWeather, position: Int) {
            binding.apply {
                // Format time
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = hourlyWeather.dt * 1000
                
                val timeText = if (position == 0) {
                    "Hiện tại"
                } else {
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
                }
                
                tvTime.text = timeText
                
                // Set temperature
                tvTemperature.text = "${hourlyWeather.temp.toCelsius()}°C"
                
                // Load weather icon
                if (hourlyWeather.weather.isNotEmpty()) {
                    val iconCode = hourlyWeather.weather[0].icon
                    WeatherIconLoader.loadWeatherIcon(iconCode, ivWeatherIcon)
                }
            }
        }
    }
}