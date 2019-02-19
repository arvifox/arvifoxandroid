package com.arvifox.arvi.simplemisc.servicehandler

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import android.widget.Toast
import com.arvifox.arvi.utils.Logger

class FooService : Service() {

    companion object {
        fun startIntent(c: Context): Intent {
            return Intent(c, FooService::class.java)
        }
    }

    private var isRunning = false
    private var looper: Looper? = null
    private var myServiceHandler: MyServiceHandler? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        val handlerthread = HandlerThread("MyThread", Process.THREAD_PRIORITY_BACKGROUND)
        handlerthread.start()
        looper = handlerthread.looper
        myServiceHandler = MyServiceHandler(looper!!)
        isRunning = true
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val msg = myServiceHandler?.obtainMessage()
        msg?.arg1 = startId
        myServiceHandler?.sendMessage(msg)
        Toast.makeText(this, "FooService Started.", Toast.LENGTH_SHORT).show()
        //If service is killed while starting, it restarts.
        return Service.START_STICKY
    }

    override fun onDestroy() {
        isRunning = false
        Toast.makeText(this, "FooService stopped", Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    private inner class MyServiceHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            synchronized(this) {
                for (i in 0..5) {
                    try {
                        Logger.d { "FooService is running...$i - " + toString() }
                        Thread.sleep(500)
                    } catch (e: Exception) {
                        Logger.d { "FooService catch =" + e.message }
                    }

                    if (!isRunning) {
                        break
                    }
                }
            }
            //stops the service for the start id.
            stopSelfResult(msg.arg1)
        }
    }
}
