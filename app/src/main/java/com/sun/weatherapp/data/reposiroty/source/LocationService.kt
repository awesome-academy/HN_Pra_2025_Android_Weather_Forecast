package com.sun.weatherapp.data.reposiroty.source

import android.location.Location
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

interface LocationService {
    fun getCurrentLocation(listener: OnResultListener<Location>)
    fun hasLocationPermission(): Boolean
}
