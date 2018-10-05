package com.arvifox.arvi.siteback

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.arvifox.arvi.BuildConfig
import com.arvifox.arvi.utils.BaseStorage
import okhttp3.*
import java.io.IOException

object BackUtils {

    fun sendToken(token: String, con: Context) {
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(BuildConfig.ARVI_API_URL + "adddevice.php?id=" + token +
                        "&tel=" + Build.MANUFACTURER.replace(" ", "", true) +
                        "_" + Build.MODEL.replace(" ", "", true))
                .build()
        val han = Handler(Looper.getMainLooper())
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                han.post { BaseStorage.tokenSent(con, false) }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    han.post { BaseStorage.tokenSent(con, true) }
                }
            }
        })
    }
}