package com.arvifox.arvi.utils

import android.content.Context

interface IPermissionManager {

    fun checkPermissions(context: Context, vararg permissions: String): Boolean
}