package com.sun.weatherapp.utils

/**
 * Extension functions for temperature conversion
 */

/**
 * Convert Kelvin to Celsius
 * @return temperature in Celsius as Integer
 */
fun Double.toCelsius(): Int {
    return (this - Constant.KELVIN_TO_CELSIUS_OFFSET).toInt()
}

/**
 * Convert Kelvin to Celsius
 * @return temperature in Celsius as Integer
 */
fun Float.toCelsius(): Int {
    return (this - Constant.KELVIN_TO_CELSIUS_OFFSET).toInt()
}

/**
 * Convert Celsius to Fahrenheit
 * @return temperature in Fahrenheit as Integer
 */
fun Double.toFahrenheit(): Int {
    return ((this - Constant.KELVIN_TO_CELSIUS_OFFSET) * 9 / 5 + 32).toInt()
}

/**
 * Convert Celsius to Fahrenheit
 * @return temperature in Fahrenheit as Integer
 */
fun Float.toFahrenheit(): Int {
    return ((this - Constant.KELVIN_TO_CELSIUS_OFFSET) * 9 / 5 + 32).toInt()
}

/**
 * Convert wind speed from m/s to km/h
 * @return wind speed in km/h as Integer
 */
fun Double.toKmPerHour(): Int {
    return (this * 3.6).toInt()
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
    return when {
        this in 0..22 || this in 338..360 -> "Bắc"
        this in 23..67 -> "Đông Bắc"
        this in 68..112 -> "Đông"
        this in 113..157 -> "Đông Nam"
        this in 158..202 -> "Nam"
        this in 203..247 -> "Tây Nam"
        this in 248..292 -> "Tây"
        this in 293..337 -> "Tây Bắc"
        else -> "Không xác định"
    }
}
