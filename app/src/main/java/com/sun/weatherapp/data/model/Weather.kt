package com.sun.weatherapp.data.model
import kotlinx.parcelize.Parcelize

import android.os.Parcelable

@Parcelize
data class WeatherResponse(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val rain: Rain?,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Long,
    val name: String,
    val cod: Int
) : Parcelable

@Parcelize
data class Coord(
    val lon: Double,
    val lat: Double
) : Parcelable

@Parcelize
data class Weather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
) : Parcelable

@Parcelize
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int,
    val sea_level: Int?,
    val grnd_level: Int?
) : Parcelable

@Parcelize
data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double?
) : Parcelable

@Parcelize
data class Rain(
    val `1h`: Double?,
    val `3h`: Double?
) : Parcelable

@Parcelize
data class Clouds(
    val all: Int
) : Parcelable

@Parcelize
data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Long,
    val sunset: Long
) : Parcelable

// OneCall API Weather Data Models
@Parcelize
data class WeatherDetailResponse(
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezone_offset: Int,
    val current: CurrentWeather,
    val hourly: List<HourlyWeather>,
    val daily: List<DailyWeather>
) : Parcelable

@Parcelize
data class CurrentWeather(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double?,
    val weather: List<Weather>
) : Parcelable

@Parcelize
data class HourlyWeather(
    val dt: Long,
    val temp: Double,
    val feels_like: Double,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double?,
    val weather: List<Weather>,
    val pop: Double,
    val rain: Rain?
) : Parcelable

@Parcelize
data class DailyWeather(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long?,
    val moonset: Long?,
    val moon_phase: Double,
    val summary: String,
    val temp: DailyTemp,
    val feels_like: DailyFeelsLike,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val wind_gust: Double?,
    val weather: List<Weather>,
    val clouds: Int,
    val pop: Double,
    val rain: Double?,
    val uvi: Double
) : Parcelable

@Parcelize
data class DailyTemp(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
) : Parcelable

@Parcelize
data class DailyFeelsLike(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
) : Parcelable

object WeatherEntry {
    const val COORD = "coord"
    const val LON = "lon"
    const val LAT = "lat"

    const val WEATHER = "weather"
    const val ID = "id"
    const val MAIN = "main"
    const val DESCRIPTION = "description"
    const val ICON = "icon"

    const val BASE = "base"

    const val MAIN_OBJ = "main"
    const val TEMP = "temp"
    const val FEELS_LIKE = "feels_like"
    const val TEMP_MIN = "temp_min"
    const val TEMP_MAX = "temp_max"
    const val PRESSURE = "pressure"
    const val HUMIDITY = "humidity"
    const val SEA_LEVEL = "sea_level"
    const val GRND_LEVEL = "grnd_level"

    const val VISIBILITY = "visibility"

    const val WIND = "wind"
    const val SPEED = "speed"
    const val DEG = "deg"
    const val GUST = "gust"

    const val RAIN = "rain"

    const val CLOUDS = "clouds"
    const val ALL = "all"

    const val DT = "dt"

    const val SYS = "sys"
    const val TYPE = "type"
    const val COUNTRY = "country"
    const val SUNRISE = "sunrise"
    const val SUNSET = "sunset"

    const val TIMEZONE = "timezone"
    const val NAME = "name"
    const val COD = "cod"
    
    // OneCall API Weather Detail entries
    const val WEATHER_DETAIL = "weather_detail"
    const val CURRENT = "current"
    const val HOURLY = "hourly"
    const val DAILY = "daily"
    const val TIMEZONE_OFFSET = "timezone_offset"
    const val WIND_SPEED = "wind_speed"
    const val WIND_DEG = "wind_deg"
    const val WIND_GUST = "wind_gust"
    const val DEW_POINT = "dew_point"
    const val UVI = "uvi"
    const val SUMMARY = "summary"
    const val MOONRISE = "moonrise"
    const val MOONSET = "moonset"
    const val MOON_PHASE = "moon_phase"
    const val POP = "pop"
    const val DAY = "day"
    const val MIN = "min"
    const val MAX = "max"
    const val NIGHT = "night"
    const val EVE = "eve"
    const val MORN = "morn"
}

enum class DailyWeatherType {
    TODAY,
    TOMORROW,
    WEEK
}