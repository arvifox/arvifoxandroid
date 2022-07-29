package com.arvifox.arvi.utils

import android.content.Context

object BaseStorage {

    const val notificationChannelID: String = "arvifox777"

    fun tokenSent(con: Context, res: Boolean) {
        val pref = con.getSharedPreferences("arvifox_storage", Context.MODE_PRIVATE)
        pref.edit().putBoolean("tokensent", res).apply()
    }

    fun isTokenSent(con: Context): Boolean {
        val pref = con.getSharedPreferences("arvifox_storage", Context.MODE_PRIVATE)
        return pref.getBoolean("tokensent", false)
    }
}