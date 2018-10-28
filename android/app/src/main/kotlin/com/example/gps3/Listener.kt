package com.example.gps3

import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.util.Log

class Listener(provider: String): LocationListener {
    val TAG = "Listener"
    val lastLocation: Location = Location(provider)


    override fun onLocationChanged(location: Location?) {
        Log.d(TAG, "onLocationChanged - $location")
        lastLocation.set(location)
    }

    override fun onStatusChanged(provider: String?, status: Int, p2: Bundle?) {
        Log.d(TAG, "onStatusChanged - $provider")
    }

    override fun onProviderEnabled(provider: String?) {
        Log.d(TAG, "onProviderEnabled - $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        Log.d(TAG, "onProviderDisabled - $provider")
    }
}