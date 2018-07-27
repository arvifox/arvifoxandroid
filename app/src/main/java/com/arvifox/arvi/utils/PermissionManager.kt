package com.arvifox.arvi.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.content.ContextCompat

class PermissionManager : IPermissionManager {

    override fun checkPermissions(context: Context, vararg permissions: String): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true
        try {
            for (st in permissions) {
                if (ContextCompat.checkSelfPermission(context, st) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        } catch (e: RuntimeException) {
            e.printStackTrace()
            return false
        }
        return true
    }
}