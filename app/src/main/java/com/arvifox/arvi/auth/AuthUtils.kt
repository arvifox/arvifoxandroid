package com.arvifox.arvi.auth

import android.util.Base64
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object AuthUtils {

    fun base() {
        val cc = Base64.encode("abra".toByteArray(), Base64.NO_WRAP)
        val huc = URL("").openConnection()
        //val hs = HttpsURLConnection()
        huc.setRequestProperty("Authorization", "Basic $cc")
    }
}