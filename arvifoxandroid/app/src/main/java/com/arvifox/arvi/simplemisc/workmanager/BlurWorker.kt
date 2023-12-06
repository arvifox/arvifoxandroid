package com.arvifox.arvi.simplemisc.workmanager

import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.arvifox.arvi.simplemisc.workmanager.WorkerUtils.blurBitmap
import com.arvifox.arvi.simplemisc.workmanager.WorkerUtils.makeStatusNotification
import com.arvifox.arvi.simplemisc.workmanager.WorkerUtils.sleep
import com.arvifox.arvi.simplemisc.workmanager.WorkerUtils.writeBitmapToFile
import com.arvifox.arvi.utils.Logger
import java.io.FileNotFoundException

class BlurWorker(c: Context, wp: WorkerParameters) : Worker(c, wp) {

    private val TAG by lazy { BlurWorker::class.java.simpleName }

    override fun doWork(): Result {
        // Makes a notification when the work starts and slows down the work so that it's easier to
        // see each WorkRequest start, even on emulated devices
        makeStatusNotification("Blurring image", applicationContext)
        sleep()
        return try {
            val outputData = createBlurredBitmap(applicationContext, inputData.getString("KEY_IMAGE_URI"))
            Result.success(outputData)
        } catch (throwable: Throwable) {
            Result.failure()
        }
    }

    @Throws(FileNotFoundException::class, IllegalArgumentException::class)
    private fun createBlurredBitmap(appContext: Context, resourceUri: String?): Data {
        if (resourceUri.isNullOrEmpty()) {
            Logger.e { "Invalid input uri" }
            throw IllegalArgumentException("Invalid input uri")
        }

        val resolver = appContext.contentResolver

        // Create a bitmap
        val bitmap = BitmapFactory.decodeStream(
                resolver.openInputStream(Uri.parse(resourceUri)))

        // Blur the bitmap
        val output = blurBitmap(bitmap, appContext)

        // Write bitmap to a temp file
        val outputUri = writeBitmapToFile(appContext, output)

        // Return the output for the temp file
        return workDataOf("KEY_IMAGE_URI" to outputUri.toString())
    }
}