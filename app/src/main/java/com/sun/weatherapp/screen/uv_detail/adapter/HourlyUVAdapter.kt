package com.sun.weatherapp.screen.uv_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.weatherapp.data.model.HourlyWeather
import com.sun.weatherapp.databinding.ItemUvHourlyBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class HourlyUVAdapter(
    private var hourlyWeatherList: List<HourlyWeather> = emptyList()
) : RecyclerView.Adapter<HourlyUVAdapter.HourlyUVViewHolder>() {

    fun updateData(newHourlyWeatherList: List<HourlyWeather>) {
        hourlyWeatherList = newHourlyWeatherList.take(24) // Chỉ lấy 24 giờ đầu
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyUVViewHolder {
        val binding = ItemUvHourlyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourlyUVViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyUVViewHolder, position: Int) {
        holder.bind(hourlyWeatherList[position], position)
    }

    override fun getItemCount(): Int = hourlyWeatherList.size

    inner class HourlyUVViewHolder(
        private val binding: ItemUvHourlyBinding
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
                
                // Set UV Index
                tvUVIndex.text = hourlyWeather.uvi.roundToInt().toString()
            }
        }
    }
}