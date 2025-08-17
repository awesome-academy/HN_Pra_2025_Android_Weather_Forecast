package com.sun.weatherapp.screen.rain_detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sun.weatherapp.databinding.ItemRainChanceBinding
import com.sun.weatherapp.data.model.HourlyWeather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class RainChanceAdapter(
    private var hourlyWeatherList: List<HourlyWeather> = emptyList()
) : RecyclerView.Adapter<RainChanceAdapter.RainChanceViewHolder>() {

    fun updateData(newHourlyWeatherList: List<HourlyWeather>) {
        hourlyWeatherList = newHourlyWeatherList.take(5)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RainChanceViewHolder {
        val binding = ItemRainChanceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RainChanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RainChanceViewHolder, position: Int) {
        holder.bind(hourlyWeatherList[position], position)
    }

    override fun getItemCount(): Int = hourlyWeatherList.size

    inner class RainChanceViewHolder(
        private val binding: ItemRainChanceBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hourlyWeather: HourlyWeather, position: Int) {
            binding.apply {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = hourlyWeather.dt * 1000
                val timeText = if (position == 0) {
                    "Now"
                } else {
                    SimpleDateFormat("h a", Locale.getDefault()).format(calendar.time)
                }
                tvTime.text = timeText
                val percentage = (hourlyWeather.pop * 100).roundToInt()
                tvPercentage.text = "${percentage}%"

                progressIndicator.progress = percentage
            }
        }
    }
}
