package com.arvifox.arvi.di

import com.arvifox.arvi.utils.IPermissionManager
import com.arvifox.arvi.utils.PermissionManager

object SimpleProvider {

    var permissionManager: IPermissionManager = PermissionManager()
}