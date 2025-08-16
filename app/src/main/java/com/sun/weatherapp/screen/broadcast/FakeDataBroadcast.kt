package com.sun.weatherapp.screen.broadcast

import com.sun.weatherapp.data.model.Clouds
import com.sun.weatherapp.data.model.Coord
import com.sun.weatherapp.data.model.Main
import com.sun.weatherapp.data.model.Rain
import com.sun.weatherapp.data.model.Sys
import com.sun.weatherapp.data.model.Weather
import com.sun.weatherapp.data.model.WeatherResponse
import com.sun.weatherapp.data.model.Wind

val sampleWeatherList = listOf(
    WeatherResponse(
        coord = Coord(105.85, 21.03),
        weather = listOf(Weather(800, "Clear", "clear sky", "01d")),
        base = "stations",
        main = Main(30.0, 32.0, 29.0, 33.0, 1010, 60, null, null),
        visibility = 10000,
        wind = Wind(3.5, 90, null),
        rain = null,
        clouds = Clouds(0),
        dt = 1620000000,
        sys = Sys(1, 1234, "VN", 1620003600, 1620049200),
        timezone = 25200,
        id = 1581130,
        name = "Hanoi",
        cod = 200
    ),
    WeatherResponse(
        coord = Coord(106.66, 10.82),
        weather = listOf(Weather(500, "Rain", "light rain", "10d")),
        base = "stations",
        main = Main(27.0, 28.0, 26.0, 29.0, 1008, 80, null, null),
        visibility = 9000,
        wind = Wind(2.0, 180, null),
        rain = Rain(0.5, null),
        clouds = Clouds(75),
        dt = 1620000001,
        sys = Sys(1, 2345, "VN", 1620003601, 1620049201),
        timezone = 25200,
        id = 1566083,
        name = "Ho Chi Minh City",
        cod = 200
    ),
    WeatherResponse(
        coord = Coord(108.21, 16.07),
        weather = listOf(Weather(801, "Clouds", "few clouds", "02d")),
        base = "stations",
        main = Main(28.0, 30.0, 27.0, 31.0, 1005, 70, null, null),
        visibility = 10000,
        wind = Wind(4.0, 150, null),
        rain = null,
        clouds = Clouds(20),
        dt = 1620000002,
        sys = Sys(1, 3456, "VN", 1620003602, 1620049202),
        timezone = 25200,
        id = 1905468,
        name = "Da Nang",
        cod = 200
    ),
    WeatherResponse(
        coord = Coord(103.98, 22.39),
        weather = listOf(Weather(802, "Clouds", "scattered clouds", "03d")),
        base = "stations",
        main = Main(20.0, 21.0, 19.0, 22.0, 1002, 85, null, null),
        visibility = 8000,
        wind = Wind(1.5, 70, null),
        rain = null,
        clouds = Clouds(40),
        dt = 1620000003,
        sys = Sys(1, 4567, "VN", 1620003603, 1620049203),
        timezone = 25200,
        id = 1562414,
        name = "Lao Cai",
        cod = 200
    ),
    WeatherResponse(
        coord = Coord(109.22, 13.77),
        weather = listOf(Weather(803, "Clouds", "broken clouds", "04d")),
        base = "stations",
        main = Main(26.0, 27.0, 25.0, 28.0, 1007, 75, null, null),
        visibility = 9500,
        wind = Wind(3.0, 200, null),
        rain = null,
        clouds = Clouds(60),
        dt = 1620000004,
        sys = Sys(1, 5678, "VN", 1620003604, 1620049204),
        timezone = 25200,
        id = 1587976,
        name = "Quy Nhon",
        cod = 200
    ),
    WeatherResponse(
        coord = Coord(104.85, 21.33),
        weather = listOf(Weather(500, "Rain", "moderate rain", "10n")),
        base = "stations",
        main = Main(23.0, 24.0, 22.0, 25.0, 1006, 88, null, null),
        visibility = 7000,
        wind = Wind(2.5, 160, null),
        rain = Rain(3.2, null),
        clouds = Clouds(90),
        dt = 1620000005,
        sys = Sys(1, 6789, "VN", 1620003605, 1620049205),
        timezone = 25200,
        id = 1586182,
        name = "Son La",
        cod = 200
    ),
    WeatherResponse(
        coord = Coord(105.63, 18.67),
        weather = listOf(Weather(804, "Clouds", "overcast clouds", "04n")),
        base = "stations",
        main = Main(25.0, 26.0, 24.0, 27.0, 1004, 82, null, null),
        visibility = 8500,
        wind = Wind(3.2, 110, null),
        rain = null,
        clouds = Clouds(100),
        dt = 1620000006,
        sys = Sys(1, 7890, "VN", 1620003606, 1620049206),
        timezone = 25200,
        id = 1586203,
        name = "Vinh",
        cod = 200
    )
)
