package com.sun.weatherapp.data.reposiroty.source

import android.location.Location
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

interface LocationDataSource {
    fun getCurrentLocation(listener: OnResultListener<Location>)
}
