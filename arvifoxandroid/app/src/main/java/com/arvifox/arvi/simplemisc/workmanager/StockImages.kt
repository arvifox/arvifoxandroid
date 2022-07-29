package com.arvifox.arvi.simplemisc.workmanager

import android.net.Uri
import java.util.*

internal object StockImages {
    private val sRandom = Random()
    private val sAssetUris = arrayOf(
            Uri.parse("file:///android_asset/images/ima1.jpg"),
            Uri.parse("file:///android_asset/images/ima2.jpg"),
            Uri.parse("file:///android_asset/images/ima3.jpg"))

    fun randomStockImage(): Uri {
        val index = sRandom.nextInt(sAssetUris.size)
        return sAssetUris[index]
    }
}
