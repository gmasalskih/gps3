package com.example.gps3

import android.Manifest
import android.annotation.SuppressLint
import android.app.IntentService
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Log

class MyIntentService : Service() {

    companion object {
        fun checkPermission(context: Context):Boolean{
            return ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    val TAG = "TAG"
    var locationManager: LocationManager? = null
    val LOCATION_INTERVAL = 1000L
    val LOCATION_DISTANCE = 10f
    private val listenerGPS: LocationListener = object :LocationListener{
        val lastLocation: Location = Location(LocationManager.GPS_PROVIDER)
        override fun onLocationChanged(location: Location?) {
            Log.d(TAG, "onLocationChanged - $location")
            lastLocation.set(location)
            val intent = Intent("my___")
            intent.putExtra("gps", location.toString())
            sendBroadcast(intent)
        }

        override fun onStatusChanged(provider: String?, status: Int, extra: Bundle?) {
            Log.d(TAG, "onStatusChanged - $provider")
        }

        override fun onProviderEnabled(provider: String?) {
            Log.d(TAG, "onProviderEnabled - $provider")
        }

        override fun onProviderDisabled(provider: String?) {
            Log.d(TAG, "onProviderDisabled - $provider")
        }
    }
    private val listenerGSM: LocationListener = object :LocationListener{
        val lastLocation: Location = Location(LocationManager.GPS_PROVIDER)
        override fun onLocationChanged(location: Location?) {
            Log.d(TAG, "onLocationChanged - $location")
            lastLocation.set(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extra: Bundle?) {
            Log.d(TAG, "onStatusChanged - $provider")
        }

        override fun onProviderEnabled(provider: String?) {
            Log.d(TAG, "onProviderEnabled - $provider")
        }

        override fun onProviderDisabled(provider: String?) {
            Log.d(TAG, "onProviderDisabled - $provider")
        }
    }

    private val locationListeners: List<LocationListener> = listOf(listenerGPS, listenerGSM)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        initializeLocationManager()
        try {
            locationManager?.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    locationListeners[1]
            )
        } catch (ex: SecurityException){
            Log.d(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException){
            Log.d(TAG, "gps provider does not exist ${ex.message}")
        }
        try {
            locationManager?.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    locationListeners[0]
            )
        } catch (ex: SecurityException){
            Log.d(TAG, "fail to request location update, ignore", ex)
        } catch (ex: IllegalArgumentException){
            Log.d(TAG, "gps provider does not exist ${ex.message}")
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        locationListeners.forEach{
            locationManager?.removeUpdates(it)
        }
    }

    private fun initializeLocationManager() {
        if (locationManager == null) {
            locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }
    }

}