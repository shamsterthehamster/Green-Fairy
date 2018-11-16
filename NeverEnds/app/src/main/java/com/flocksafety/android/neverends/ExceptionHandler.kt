package com.flocksafety.android.neverends

import android.content.Context
import android.os.Process
import android.util.Log

class ExceptionHandler(context: Context): java.lang.Thread.UncaughtExceptionHandler {
    private val context = context
    val LOG_NAME = "NES-ExceptionHandler'"

    override fun uncaughtException(thread: Thread, exception: Throwable) {
        Log.i(LOG_NAME, "Caught Exception: $exception")
        Log.i(LOG_NAME, "Killing Process")
        Process.killProcess(Process.myPid())
        System.exit(0)
    }

}