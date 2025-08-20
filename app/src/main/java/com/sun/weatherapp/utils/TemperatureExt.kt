package com.sun.weatherapp.utils

import java.util.Calendar
import kotlin.math.roundToInt

/**
 * Extension functions for temperature conversion
 */

/**
 * Convert Kelvin to Celsius
 * @return temperature in Celsius as Integer
 */
fun Double.toCelsius(): Int {
    return (this - Constant.KELVIN_TO_CELSIUS_OFFSET).roundToInt()
}

/**
 * Convert Kelvin to Celsius
 * @return temperature in Celsius as Integer
 */
fun Float.toCelsius(): Int {
    return (this - Constant.KELVIN_TO_CELSIUS_OFFSET).roundToInt()
}

/**
 * Convert Celsius to Fahrenheit
 * @return temperature in Fahrenheit as Integer
 */
fun Double.toFahrenheit(): Int {
    return ((this - Constant.KELVIN_TO_CELSIUS_OFFSET) * 9 / 5 + 32).roundToInt()
}

/**
 * Convert Celsius to Fahrenheit
 * @return temperature in Fahrenheit as Integer
 */
fun Float.toFahrenheit(): Int {
    return ((this - Constant.KELVIN_TO_CELSIUS_OFFSET) * 9 / 5 + 32).roundToInt()
}

/**
 * Convert wind speed from m/s to km/h
 * @return wind speed in km/h as Integer
 */
fun Double.toKmPerHour(): Int {
    return (this * 3.6).roundToInt()
}

/**
 * Convert wind speed from m/s to km/h
 * @return wind speed in km/h as Float with 1 decimal place
 */
fun Double.toKmPerHourFloat(): Float {
    return (this * 3.6).toFloat()
}

/**
 * Convert wind degree to direction text
 * @return wind direction as String
 */
fun Int.toWindDirection(): String {
    return when (this) {
        in 0..22, in 338..360 -> "Bắc"
        in 23..67 -> "Đông Bắc"
        in 68..112 -> "Đông"
        in 113..157 -> "Đông Nam"
        in 158..202 -> "Nam"
        in 203..247 -> "Tây Nam"
        in 248..292 -> "Tây"
        in 293..337 -> "Tây Bắc"
        else -> "Không xác định"
    }
}
