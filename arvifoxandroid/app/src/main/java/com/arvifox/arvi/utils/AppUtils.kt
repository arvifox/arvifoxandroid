package com.arvifox.arvi.utils

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process

val Application.currentProcessName: String?
    get() {
        val pid = Process.myPid()
        val am = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager?
        return am?.runningAppProcesses?.find { pid == it.pid }?.processName
    }

val Application.isPrimaryProcess: Boolean
    get() = currentProcessName == packageName

fun Application.runIfPrimaryProcess(block: () -> Unit) {
    if (isPrimaryProcess) {
        block.invoke()
    }
}
