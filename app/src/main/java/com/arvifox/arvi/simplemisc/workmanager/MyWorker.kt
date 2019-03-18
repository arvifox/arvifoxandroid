package com.arvifox.arvi.simplemisc.workmanager

import android.content.Context
import android.os.Looper
import androidx.work.*

class MyWorker(c: Context, wp: WorkerParameters) : Worker(c, wp) {
    override fun doWork(): Result {
        //do the work you want done in the background here
        return Result.success()
    }
}