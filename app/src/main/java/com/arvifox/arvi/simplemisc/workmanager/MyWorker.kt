package com.arvifox.arvi.simplemisc.workmanager

import android.content.Context
import androidx.work.*

class MyWorker(c: Context, wp: WorkerParameters) : Worker(c, wp) {
    override fun doWork(): Result {
        //do the work you want done in the background here
        return Result.success()
    }

    // optionally, add constraints like power, network availability
    private val constraints: Constraints = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    val myOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
            .setConstraints(constraints).build()
}