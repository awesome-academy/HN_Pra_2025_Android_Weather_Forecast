package com.sun.weatherapp.data.reposiroty.source.local

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.sun.weatherapp.data.reposiroty.source.LocationService
import com.sun.weatherapp.data.reposiroty.source.remote.OnResultListener

class LocationServiceImpl(
    private val context: Context,
    private val fusedLocationClient: FusedLocationProviderClient
) : LocationService {

    override fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    override fun getCurrentLocation(listener: OnResultListener<Location>) {
        if (!hasLocationPermission()) {
            listener.onError(SecurityException("Location permission not granted"))
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.i("LocationService", "Location found: ${location.latitude}, ${location.longitude}")
                    listener.onSuccess(location)
                } else {
                    Log.w("LocationService", "Location is null")
                    listener.onError(Exception("Location is null"))
                }
            }
            .addOnFailureListener { exception ->
                Log.e("LocationService", "Failed to get location", exception)
                listener.onError(exception)
            }
    }
}
