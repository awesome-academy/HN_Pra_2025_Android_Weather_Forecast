package com.sun.weatherapp.utils

import android.util.Log
import org.json.JSONObject
import java.net.HttpURLConnection
import java.text.SimpleDateFormat
import java.util.*

object SimpleApiLogger {
    private const val TAG = "API_LOG"
    private val dateFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
    
    fun logRequest(url: String, method: String = "GET") {
        val time = dateFormat.format(Date())
        Log.d(TAG, "🚀 [$time] REQUEST: $method $url")
    }
    
    fun logResponse(url: String, responseCode: Int, responseBody: String?, duration: Long) {
        val time = dateFormat.format(Date())
        val status = if (responseCode in 200..299) "✅" else "❌"
        
        Log.d(TAG, "$status [$time] RESPONSE: $responseCode | ${duration}ms")
        Log.d(TAG, "URL: $url")
        
        responseBody?.let {
            val formattedBody = try {
                JSONObject(it).toString(2)
            } catch (e: Exception) {
                it
            }
            Log.d(TAG, "BODY: $formattedBody")
        }
        Log.d(TAG, "─────────────────────────────────────")
    }
    
    fun logError(url: String, error: Exception, duration: Long) {
        val time = dateFormat.format(Date())
        Log.e(TAG, "💥 [$time] ERROR: ${error.message} | ${duration}ms")
        Log.e(TAG, "URL: $url")
        Log.e(TAG, "─────────────────────────────────────")
    }
    
    fun logCurl(url: String, method: String = "GET") {
        Log.d(TAG, "🔧 CURL: curl -X $method \"$url\"")
    }
}
