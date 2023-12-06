package com.arvifox.arvi.simplemisc.workmanager

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class MyWorker(c: Context, wp: WorkerParameters) : Worker(c, wp) {
    override fun doWork(): Result {
        //do the work you want done in the background here
        val id = inputData
        return Result.success()
    }
}