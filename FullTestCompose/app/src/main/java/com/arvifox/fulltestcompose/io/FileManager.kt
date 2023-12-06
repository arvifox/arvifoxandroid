package com.arvifox.fulltestcompose.io

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import java.io.InputStream

interface FileManager {
    fun readAssetFile(fileName: String): String
    fun readAssetFileToStream(fileName: String): InputStream
    fun readInternalCacheFile(fileName: String): String?
    fun writeInternalCacheFile(fileName: String, content: String)
    fun writeExternalCacheBitmap(
        bitmap: Bitmap,
        fileName: String,
        format: Bitmap.CompressFormat,
        quality: Int
    ): Uri

    fun writeExternalCacheText(fileName: String, body: String): Uri
    val logStorageDir: String
}

fun buildSvgImage(context: Context): ImageRequest {
    return ImageRequest.Builder(context)
        //.data("file:///android_asset/ic_0x004c.svg")
        .data("file:///android_asset/ic_0x0033.png")
        .decoderFactory(SvgDecoder.Factory())
        .build()
}

fun buildImageToBitmap(fm: FileManager, fileName: String) =
    BitmapFactory.decodeStream(fm.readAssetFileToStream(fileName))
