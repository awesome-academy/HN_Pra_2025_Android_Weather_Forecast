package com.sun.mvp.data.repository.source.remote.fetchjson

import android.util.Log
import com.sun.weatherapp.data.model.WeatherEntry
import com.sun.weatherapp.utils.notNull
import org.json.JSONException
import org.json.JSONObject

class ParseDataWithJson {
    fun parseJsonToData(jsonObject: JSONObject?, keyEntity: String): Any {
        val data = mutableListOf<Any>()
        try {
            val jsonArray = jsonObject?.getJSONArray(keyEntity)
            for (i in 0 until (jsonArray?.length() ?: 0)) {
                val item = parseJsonToObject(jsonArray?.getJSONObject(i), keyEntity)
                item.notNull {
                    data.add(it)
                }
            }
        } catch (e: JSONException) {
            Log.e("ParseDataWithJson", "parseJsonToData: ", e)
        }
        return data
    }

    private fun parseJsonToObject(jsonObject: JSONObject?, keyEntity: String): Any? {
        try {
            jsonObject?.notNull {
                return when (keyEntity) {
                    WeatherEntry.WEATHER -> ParseJson().parseWeatherJson(it)
                    else -> null
                }
            }
        } catch (e: JSONException) {
            Log.e("ParseDataWithJson", "parseJsonToData: ", e)
        }
        return null
    }
}
