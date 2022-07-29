package com.arvifox.arvi.google.utils

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import com.arvifox.arvi.utils.Logger

object ArUtils {

    private const val MIN_OPEN_GL_VERSION = 3.0

    /**
     * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
     * on this device.
     * Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
     * Finishes the activity if Sceneform can not run
     */
    fun checkIsSupportedDeviceOrFinish(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Logger.e("arvifox") { "Sceneform requires Android N or later" }
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show()
            activity.finish()
            return false
        }
        val openGlVersionString = (activity.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
                .deviceConfigurationInfo
                .glEsVersion
        if (java.lang.Double.parseDouble(openGlVersionString) < MIN_OPEN_GL_VERSION) {
            Logger.e("arvifox") { "Sceneform requires OpenGL ES 3.0 later" }
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show()
            activity.finish()
            return false
        }
        return true
    }
}