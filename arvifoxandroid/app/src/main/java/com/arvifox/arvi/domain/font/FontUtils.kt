package com.arvifox.arvi.domain.font

import android.content.Context
import android.provider.Settings

object FontUtils {
    fun change(cn: Context) {
        Settings.System.putFloat(cn.contentResolver, Settings.System.FONT_SCALE, 0.9f)
    }
}