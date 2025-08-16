package com.sun.weatherapp.utils

import android.graphics.Bitmap
import android.widget.ImageView
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

object WeatherIconLoader {
    
    private const val ICON_BASE_URL = "http://openweathermap.org/img/w/"
    private const val ICON_EXTENSION = ".png"
    
    private val iconCache = mutableMapOf<String, Bitmap>()
    
    /**
     * Load weather icon từ API và set vào ImageView
     * @param iconCode: mã icon từ API (ví dụ: "01d")
     * @param imageView: ImageView để hiển thị icon
     */
    fun loadWeatherIcon(iconCode: String, imageView: ImageView) {
        iconCache[iconCode]?.let { cachedBitmap ->
            imageView.setImageBitmap(cachedBitmap)
            return
        }
        
        val iconUrl = "$ICON_BASE_URL$iconCode$ICON_EXTENSION"
        
        SimpleApiLogger.logRequest(iconUrl, "GET")
        SimpleApiLogger.logCurl(iconUrl, "GET")
        
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        val startTime = System.currentTimeMillis()
        
        executor.execute {
            try {
                val url = URL(iconUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connectTimeout = 10000
                connection.readTimeout = 10000
                connection.connect()
                
                val inputStream: InputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val duration = System.currentTimeMillis() - startTime
                
                bitmap?.let { iconCache[iconCode] = it }
                SimpleApiLogger.logResponse(iconUrl, connection.responseCode, "Icon loaded successfully", duration)
                
                handler.post {
                    bitmap?.let { imageView.setImageBitmap(it) }
                }
                
                inputStream.close()
                connection.disconnect()
                
            } catch (e: Exception) {
                val duration = System.currentTimeMillis() - startTime
                SimpleApiLogger.logError(iconUrl, e, duration)
                
                handler.post {
                    imageView.setImageResource(com.sun.weatherapp.R.drawable.ic_cloud_and_sun)
                }
            }
        }
    }
}