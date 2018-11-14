package com.flocksafety.android.neverends

import android.app.Service
import android.content.ComponentCallbacks2
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.support.annotation.Nullable
import android.util.Log
import java.io.File
import java.io.FileInputStream

import java.util.Timer
import java.util.TimerTask

class NEService(context: Context?) : Service(), ComponentCallbacks2 {
    val LOG_NAME = "NE-Service"

    private var context: Context?
    private var counter = 0
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var exceptionHandler: ExceptionHandler? = null

    init {
        Log.i(LOG_NAME, "Start")
        this.context = context
    }

    constructor() : this(null) {
    }

    //need static variable to see if service is already running (otherwise starts timer twice, makes it go twice as fast)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        if (timer == null) {
            startTimer()
            launchExceptHandler()
        }
        return START_STICKY
    }

    override fun onTrimMemory(level: Int) {
        Log.i(LOG_NAME, "onTrimMemory has been called")
//        Process.killProcess(Process.myPid())
//        System.exit(0)
        onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        onDestroy()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(LOG_NAME, "onDestroy")
        val broadcastIntent = Intent("com.flocksafety.android.ActivityRecognition.RestartService")
        broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        sendBroadcast(broadcastIntent)
        stopTimerTask()
    }

    private fun startTimer() {
        timer = Timer()
        initializeTimerTask()
        timer?.schedule(timerTask, 1000, 1000)
    }

    private fun initializeTimerTask() {
        timerTask = object: TimerTask() {
            override fun run() {
                Log.i(LOG_NAME, "count ++++ " + (counter++))
//                if (counter == 30) {
//                    val file_size = readLargeFileJustForFun()
//                    Log.i(LOG_NAME, "Finished reading file of $file_size bytes")
//                }
            }
        }
    }

    private fun stopTimerTask() {
        timer?.let {
            it.cancel()
            timer = null
        }
    }

    private fun launchExceptHandler() {
        exceptionHandler = ExceptionHandler(this)
        Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)
    }

    private fun readLargeFileJustForFun(): Int {
        val file = File("/storage/emulated/0/PumpkinPatch.jpg")
        val fileInputStream = FileInputStream(file)
        val list: MutableList<Int> = ArrayList<Int>()
        val file_size = fileInputStream.available()
        while (fileInputStream.available() > 0) {
            list.add(fileInputStream.read())
        }
        return file_size
    }

    @Nullable override fun onBind(intent: Intent): IBinder? {
        return null
    }


}