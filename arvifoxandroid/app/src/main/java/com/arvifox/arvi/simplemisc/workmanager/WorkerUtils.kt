package com.arvifox.arvi.simplemisc.workmanager

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.NonNull
import androidx.annotation.WorkerThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.Logger

import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

object WorkerUtils {
    /**
     * Create a Notification that is shown as a heads-up notification if possible.
     *
     * For this codelab, this is used to show a notification so that you know when different steps
     * of the background work chain are starting
     *
     * @param message Message shown on the notification
     * @param context Context needed to create Toast
     */
    fun makeStatusNotification(message: String, context: Context) {
        // Make a channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            val name = "cha name"
            val description = "cha desc"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("123123", name, importance)
            channel.description = description

            // Add the channel
            val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)
        }
        // Create the notification
        val builder = NotificationCompat.Builder(context, "123123")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("notif title")
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(longArrayOf(123))

        // Show the notification
        NotificationManagerCompat.from(context).notify(456, builder.build())
    }

    /**
     * Method for sleeping for a fixed about of time to emulate slower work
     */
    fun sleep() {
        try {
            Thread.sleep(1000, 0)
        } catch (e: InterruptedException) {
            Logger.d { e.message!! }
        }
    }

    /**
     * Blurs the given Bitmap image
     * @param bitmap Image to blur
     * @param applicationContext Application context
     * @return Blurred bitmap image
     */
    @SuppressLint("NewApi")
    @WorkerThread
    fun blurBitmap(@NonNull bitmap: Bitmap,
                   @NonNull applicationContext: Context): Bitmap {
        var rsContext: RenderScript? = null
        try {

            // Create the output bitmap
            val output = Bitmap.createBitmap(
                    bitmap.width, bitmap.height, bitmap.config)
            // Blur the image
            rsContext = RenderScript.create(applicationContext, RenderScript.ContextType.DEBUG)
            val inAlloc = Allocation.createFromBitmap(rsContext, bitmap)
            val outAlloc = Allocation.createTyped(rsContext, inAlloc.type)
            val theIntrinsic =
                    ScriptIntrinsicBlur.create(rsContext, Element.U8_4(rsContext))
            theIntrinsic.setRadius(10f)
            theIntrinsic.setInput(inAlloc)
            theIntrinsic.forEach(outAlloc)
            outAlloc.copyTo(output)
            return output
        } finally {
            rsContext?.finish()
        }
    }

    /**
     * Writes bitmap to a temporary file and returns the Uri for the file
     * @param applicationContext Application context
     * @param bitmap Bitmap to write to temp file
     * @return Uri for temp file with bitmap
     * @throws FileNotFoundException Throws if bitmap file cannot be found
     */
    fun writeBitmapToFile(
            @NonNull applicationContext: Context,
            @NonNull bitmap: Bitmap): Uri {
        val name = String.format("blur-filter-output-%s.png", UUID.randomUUID().toString())
        val outputDir = File(applicationContext.filesDir, "output_path")
        if (!outputDir.exists()) {
            outputDir.mkdirs() // should succeed
        }
        val outputFile = File(outputDir, name)
        val out = FileOutputStream(outputFile)
        try {
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /* ignored for PNG */, out)
        } finally {
            try {
                out.close()
            } catch (ignore: IOException) {
            }
        }
        return Uri.fromFile(outputFile)
    }
}