package com.flocksafety.android.neverends

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class OnBootReceiver(): BroadcastReceiver() {
    val LOG_NAME = "NE-OnBootReceiver"
    val SERVICE_TYPE = NEService::class.java

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(LOG_NAME, "Just Booted")
        context.startService(Intent(context, SERVICE_TYPE))
    }
}