package com.arvifox.arvi.domain

import android.content.Context
import android.os.Environment
import android.os.StatFs

fun tret(c: Context) {
    val xx = c.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val stat = StatFs(xx?.path)
    val bsl = stat.blockSizeLong
    val abl = stat.availableBlocksLong
    val result = abl.toFloat() * bsl.toFloat() / (1024f*1024f*1024f)
}