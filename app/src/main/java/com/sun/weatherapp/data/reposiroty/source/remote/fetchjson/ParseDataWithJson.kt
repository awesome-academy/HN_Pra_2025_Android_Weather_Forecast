package com.sun.mvp.data.repository.source.remote.fetchjson

import android.util.Log
import com.sun.weatherapp.data.model.WeatherEntry
import com.sun.weatherapp.utils.notNull
import org.json.JSONException
import org.json.JSONObject

class ParseDataWithJson {
    fun parseJsonToData(jsonObject: JSONObject?, keyEntity: String): Any? {
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
