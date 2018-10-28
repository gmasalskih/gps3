package com.example.gps3

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.app.ActivityCompat

import io.flutter.app.FlutterActivity
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

class MainActivity(): FlutterActivity() {
  private val GPS = "gmasalskih.ru/gps"
  private val GPS_RESULT = "gmasalskih.ru/get_gps_result"
  private var flag = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    GeneratedPluginRegistrant.registerWith(this)
    showPermissionDialog()
    EventChannel(flutterView, GPS_RESULT).setStreamHandler(object : EventChannel.StreamHandler{

      override fun onListen(param: Any?, event: EventChannel.EventSink?) {
        registerReceiver(aaa(event), IntentFilter("my___"))
      }

      override fun onCancel(param: Any?) {
      }

    })

    MethodChannel(flutterView, GPS).setMethodCallHandler { methodCall, _ ->
      if(methodCall.method == "runGPS") runGPS()
      if(methodCall.method == "stopGPS") stopGPS()
    }
  }

  fun aaa(event: EventChannel.EventSink?): BroadcastReceiver{
    return object : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        if (flag)event?.success(intent?.getStringExtra("gps"))
        else event?.success("No Data")
      }
    }
  }

  private fun runGPS(){
    flag = true
    startService(Intent(this, MyIntentService::class.java))
  }
  private fun stopGPS(){
    flag = false
    stopService(Intent(this, MyIntentService::class.java))
  }

  private fun showPermissionDialog(){
    if (!MyIntentService.checkPermission(this)){
      ActivityCompat.requestPermissions(this, arrayOf(
              Manifest.permission.ACCESS_COARSE_LOCATION,
              Manifest.permission.ACCESS_FINE_LOCATION
      ), 0)
    }
  }

}