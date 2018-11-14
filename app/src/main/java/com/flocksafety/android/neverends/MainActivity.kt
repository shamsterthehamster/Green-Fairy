package com.flocksafety.android.neverends

import android.Manifest
import android.app.Activity
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.support.v4.app.ActivityCompat
import android.util.Log

class MainActivity : AppCompatActivity() {

    /*
    Did test: close activity, kill process, send memory trim, restart, file handling exception
    CONSTRAINTS:
        - Will not work with Android 8 or above (broadcast receiver cannot be defined in Manifest)
          (This just means it won't start on reboot. Look at how to start automatically in ES)
        - Cannot restart after a force stop
        - Memory constraints for image processing have not been tested
     */

    val THREE_SECONDS: Long = 3*1000
    val FIFTEEN_SECONDS: Long = 15*1000
    val THIRTY_SECONDS: Long = 30*1000

    val LOG_NAME = "NE-MainActivity"
    val SERVICE_TYPE = NEService::class.java

    var serviceIntent: Intent? = null
    private var service: NEService? = null
    var context: Context? = null
    var alarmManager: AlarmManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_main)
//        askPermissionToReadExternalStorage()
        Log.i(LOG_NAME, "Launching Service")
        launchService()
        setAlarm(THREE_SECONDS)
    }

    private fun launchService() {
        Log.i(LOG_NAME, context.toString())
        service = NEService(context)
        serviceIntent = Intent(context, SERVICE_TYPE)
        startService(serviceIntent)
    }

    private fun setAlarm(frequency: Long) {
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ACTION_MAIN)
        intent.addCategory(CATEGORY_LAUNCHER)
        intent.setComponent(componentName)
        intent.addFlags(FLAG_INCLUDE_STOPPED_PACKAGES)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
        val startTime = System.currentTimeMillis() + frequency
        alarmManager?.setRepeating(AlarmManager.RTC, startTime, frequency, pendingIntent)
    }

    private fun askPermissionToReadExternalStorage() {
        if (context == null) {
            Log.i(LOG_NAME, "context is null tho")
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                0
            )
        }
    }

    override fun onDestroy() {
        stopService(serviceIntent)
        Log.i(LOG_NAME, "onDestroy")
        super.onDestroy()
    }
}
