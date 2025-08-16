package com.sun.weatherapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import android.graphics.drawable.Drawable

object WeatherIconLoader {
    
    private const val ICON_BASE_URL = "http://openweathermap.org/img/w/"
    private const val ICON_EXTENSION = ".png"
    
    /**
     * @param iconCode: mã icon từ API (ví dụ: "01d")
     * @param imageView: ImageView để hiển thị icon
     */
    fun loadWeatherIcon(iconCode: String, imageView: ImageView) {
        val iconUrl = "$ICON_BASE_URL$iconCode$ICON_EXTENSION"
        val startTime = System.currentTimeMillis()
        
        SimpleApiLogger.logRequest(iconUrl, "GET")
        SimpleApiLogger.logCurl(iconUrl, "GET")
        
        Glide.with(imageView.context)
            .load(iconUrl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(com.sun.weatherapp.R.drawable.ic_cloud_and_sun)
            .error(com.sun.weatherapp.R.drawable.ic_cloud_and_sun)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>,
                    isFirstResource: Boolean
                ): Boolean {
                    val duration = System.currentTimeMillis() - startTime
                    SimpleApiLogger.logError(iconUrl, Exception(e?.message ?: "Glide load failed"), duration)
                    return false
                }
                
                override fun onResourceReady(
                    resource: Drawable,
                    model: Any,
                    target: Target<Drawable>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    val duration = System.currentTimeMillis() - startTime
                    val cacheStatus = when(dataSource) {
                        DataSource.MEMORY_CACHE -> "Memory Cache"
                        DataSource.RESOURCE_DISK_CACHE -> "Disk Cache"
                        DataSource.DATA_DISK_CACHE -> "Data Cache"
                        DataSource.REMOTE -> "Network"
                        else -> "Unknown"
                    }
                    SimpleApiLogger.logResponse(iconUrl, 200, "Icon loaded from $cacheStatus", duration)
                    return false
                }

            })
            .into(imageView)
    }
}
