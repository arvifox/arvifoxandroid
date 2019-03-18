package com.arvifox.arvi.simplemisc.workmanager

import android.content.Context
import android.graphics.BitmapFactory
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.arvifox.arvi.R

import com.arvifox.arvi.simplemisc.workmanager.WorkerUtils.blurBitmap
import com.arvifox.arvi.simplemisc.workmanager.WorkerUtils.writeBitmapToFile

class BlurWorker(c: Context, wp: WorkerParameters) : Worker(c, wp) {

    private val TAG by lazy { BlurWorker::class.java.simpleName }

    override fun doWork(): Result {
        return try {
            val b = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.soccer_bal)
            val output = blurBitmap(b, applicationContext)
            val outputUri = writeBitmapToFile(applicationContext, output)
            Result.success()
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }
}