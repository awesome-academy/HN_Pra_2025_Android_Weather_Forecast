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
