package com.flocksafety.android.neverends

import android.content.Context
import android.content.Intent
import android.support.v4.app.JobIntentService

class JobService(): JobIntentService() {
    val JOB_ID = 0x01

    fun enqueueWork(context: Context, work: Intent) {
        enqueueWork(context, NEService::class.java, JOB_ID, work)
    }

    override fun onHandleWork(intent: Intent) {
        this.startService(intent)
    }
}