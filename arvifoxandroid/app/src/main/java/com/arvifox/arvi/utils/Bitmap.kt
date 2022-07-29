package com.arvifox.arvi.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.annotation.RawRes

object BitmapFoo {

    fun loadBitmap(
        context: Context, @RawRes resourceId: Int,
        bitmapConfig: Bitmap.Config
    ): Bitmap? {
        val options =
            BitmapFactory.Options().apply { inPreferredConfig = bitmapConfig }
        return BitmapFactory.decodeResource(
            context.resources, resourceId, options
        )
    }
}