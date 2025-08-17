package com.sun.weatherapp.screen.pressure_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.weatherapp.data.model.HourlyWeather
import com.sun.weatherapp.databinding.ItemPressureHourlyBinding
import java.text.SimpleDateFormat
import java.util.*

class HourlyPressureAdapter(
    private var hourlyWeatherList: List<HourlyWeather> = emptyList()
) : RecyclerView.Adapter<HourlyPressureAdapter.HourlyPressureViewHolder>() {

    fun updateData(newHourlyWeatherList: List<HourlyWeather>) {
        hourlyWeatherList = newHourlyWeatherList.take(24)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyPressureViewHolder {
        val binding = ItemPressureHourlyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HourlyPressureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HourlyPressureViewHolder, position: Int) {
        holder.bind(hourlyWeatherList[position], position)
    }

    override fun getItemCount(): Int = hourlyWeatherList.size

    inner class HourlyPressureViewHolder(
        private val binding: ItemPressureHourlyBinding
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
                
                // Set pressure
                tvPressure.text = "${hourlyWeather.pressure}hPa"
            }
        }
    }
}
