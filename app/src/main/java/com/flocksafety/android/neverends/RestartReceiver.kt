package com.flocksafety.android.neverends

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class RestartReceiver(): BroadcastReceiver() {
    val SERVICE_TYPE = NEService::class.java
    val LOG_NAME = "NE-RestartReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(LOG_NAME, "called")
        val restart_intent = Intent(context, SERVICE_TYPE)
        context.startService(restart_intent)
    }

}