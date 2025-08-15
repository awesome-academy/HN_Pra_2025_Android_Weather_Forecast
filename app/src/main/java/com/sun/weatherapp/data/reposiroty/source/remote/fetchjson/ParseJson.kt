package com.sun.mvp.data.repository.source.remote.fetchjson

import com.sun.weatherapp.data.model.*
import org.json.JSONArray
import org.json.JSONObject

class ParseJson {

    fun parseWeatherJson(jsonObject: JSONObject) = WeatherResponse(
        coord = jsonObject.getJSONObject(WeatherEntry.COORD).run {
            Coord(
                lon = getDouble(WeatherEntry.LON),
                lat = getDouble(WeatherEntry.LAT)
            )
        },
        weather = jsonObject.getJSONArray(WeatherEntry.WEATHER).parseWeatherList(),
        base = jsonObject.getString(WeatherEntry.BASE),
        main = jsonObject.getJSONObject(WeatherEntry.MAIN).run {
            Main(
                temp = getDouble(WeatherEntry.TEMP),
                feels_like = getDouble(WeatherEntry.FEELS_LIKE),
                temp_min = getDouble(WeatherEntry.TEMP_MIN),
                temp_max = getDouble(WeatherEntry.TEMP_MAX),
                pressure = getInt(WeatherEntry.PRESSURE),
                humidity = getInt(WeatherEntry.HUMIDITY),
                sea_level = optInt(WeatherEntry.SEA_LEVEL),
                grnd_level = optInt(WeatherEntry.GRND_LEVEL)
            )
        },
        visibility = jsonObject.getInt(WeatherEntry.VISIBILITY),
        wind = jsonObject.getJSONObject(WeatherEntry.WIND).run {
            Wind(
                speed = getDouble(WeatherEntry.SPEED),
                deg = getInt(WeatherEntry.DEG),
                gust = optDouble(WeatherEntry.GUST, Double.NaN).takeIf { !it.isNaN() }
            )
        },
        rain = jsonObject.optJSONObject(WeatherEntry.RAIN)?.run {
            Rain(`1h` = getDouble("1h"))
        },
        clouds = jsonObject.getJSONObject(WeatherEntry.CLOUDS).run {
            Clouds(all = getInt(WeatherEntry.ALL))
        },
        dt = jsonObject.getLong(WeatherEntry.DT),
        sys = jsonObject.getJSONObject(WeatherEntry.SYS).run {
            Sys(
                type = optInt(WeatherEntry.TYPE, 0).takeIf { has(WeatherEntry.TYPE) },
                id = optInt(WeatherEntry.ID, 0).takeIf { has(WeatherEntry.ID) },
                country = optString(WeatherEntry.COUNTRY).takeIf { has(WeatherEntry.COUNTRY) },
                sunrise = getLong(WeatherEntry.SUNRISE),
                sunset = getLong(WeatherEntry.SUNSET)
            )
        },
        timezone = jsonObject.getInt(WeatherEntry.TIMEZONE),
        id = jsonObject.getLong(WeatherEntry.ID),
        name = jsonObject.getString(WeatherEntry.NAME),
        cod = jsonObject.getInt(WeatherEntry.COD)
    )

    fun parseWindDetailJson(jsonObject: JSONObject) = WindDetailResponse(
        lat = jsonObject.getDouble(WeatherEntry.LAT),
        lon = jsonObject.getDouble(WeatherEntry.LON),
        timezone = jsonObject.getString(WeatherEntry.TIMEZONE),
        timezone_offset = jsonObject.getInt(WeatherEntry.TIMEZONE_OFFSET),
        current = jsonObject.getJSONObject(WeatherEntry.CURRENT).run {
            CurrentWind(
                dt = getLong(WeatherEntry.DT),
                sunrise = getLong(WeatherEntry.SUNRISE),
                sunset = getLong(WeatherEntry.SUNSET),
                temp = getDouble(WeatherEntry.TEMP),
                feels_like = getDouble(WeatherEntry.FEELS_LIKE),
                pressure = getInt(WeatherEntry.PRESSURE),
                humidity = getInt(WeatherEntry.HUMIDITY),
                dew_point = getDouble(WeatherEntry.DEW_POINT),
                uvi = getDouble(WeatherEntry.UVI),
                clouds = getInt(WeatherEntry.CLOUDS),
                visibility = getInt(WeatherEntry.VISIBILITY),
                wind_speed = getDouble(WeatherEntry.WIND_SPEED),
                wind_deg = getInt(WeatherEntry.WIND_DEG),
                wind_gust = optDouble(WeatherEntry.WIND_GUST, Double.NaN).takeIf { !it.isNaN() },
                weather = getJSONArray(WeatherEntry.WEATHER).parseWeatherList(),
                rain = optJSONObject(WeatherEntry.RAIN)?.run {
                    Rain(`1h` = getDouble("1h"))
                }
            )
        },
        daily = jsonObject.getJSONArray(WeatherEntry.DAILY).parseDailyWindList()
    )

    private fun JSONArray.parseDailyWindList(): List<DailyWind> {
        val list = mutableListOf<DailyWind>()
        for (i in 0 until length()) {
            getJSONObject(i).apply {
                list.add(
                    DailyWind(
                        dt = getLong(WeatherEntry.DT),
                        sunrise = getLong(WeatherEntry.SUNRISE),
                        sunset = getLong(WeatherEntry.SUNSET),
                        moonrise = optLong(WeatherEntry.MOONRISE, 0).takeIf { has(WeatherEntry.MOONRISE) && it != 0L },
                        moonset = optLong(WeatherEntry.MOONSET, 0).takeIf { has(WeatherEntry.MOONSET) && it != 0L },
                        moon_phase = getDouble(WeatherEntry.MOON_PHASE),
                        summary = getString(WeatherEntry.SUMMARY),
                        temp = getJSONObject(WeatherEntry.TEMP).run {
                            DailyTemp(
                                day = getDouble(WeatherEntry.DAY),
                                min = getDouble(WeatherEntry.MIN),
                                max = getDouble(WeatherEntry.MAX),
                                night = getDouble(WeatherEntry.NIGHT),
                                eve = getDouble(WeatherEntry.EVE),
                                morn = getDouble(WeatherEntry.MORN)
                            )
                        },
                        feels_like = getJSONObject(WeatherEntry.FEELS_LIKE).run {
                            DailyFeelsLike(
                                day = getDouble(WeatherEntry.DAY),
                                night = getDouble(WeatherEntry.NIGHT),
                                eve = getDouble(WeatherEntry.EVE),
                                morn = getDouble(WeatherEntry.MORN)
                            )
                        },
                        pressure = getInt(WeatherEntry.PRESSURE),
                        humidity = getInt(WeatherEntry.HUMIDITY),
                        dew_point = getDouble(WeatherEntry.DEW_POINT),
                        wind_speed = getDouble(WeatherEntry.WIND_SPEED),
                        wind_deg = getInt(WeatherEntry.WIND_DEG),
                        wind_gust = optDouble(WeatherEntry.WIND_GUST, Double.NaN).takeIf { !it.isNaN() },
                        weather = getJSONArray(WeatherEntry.WEATHER).parseWeatherList(),
                        clouds = getInt(WeatherEntry.CLOUDS),
                        pop = getDouble(WeatherEntry.POP),
                        rain = optDouble(WeatherEntry.RAIN, Double.NaN).takeIf { !it.isNaN() },
                        uvi = getDouble(WeatherEntry.UVI)
                    )
                )
            }
        }
        return list
    }

    private fun JSONArray.parseWeatherList(): List<Weather> {
        val list = mutableListOf<Weather>()
        for (i in 0 until length()) {
            getJSONObject(i).apply {
                list.add(
                    Weather(
                        id = getInt(WeatherEntry.ID),
                        main = getString(WeatherEntry.MAIN),
                        description = getString(WeatherEntry.DESCRIPTION),
                        icon = getString(WeatherEntry.ICON)
                    )
                )
            }
        }
        return list
    }
}
