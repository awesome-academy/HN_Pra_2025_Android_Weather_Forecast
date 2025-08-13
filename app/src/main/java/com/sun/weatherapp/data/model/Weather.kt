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
    val `1h`: Double
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
}
