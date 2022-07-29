package com.arvifox.arvi.domain.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager

/*

https://habr.com/ru/post/467765/

SSID — Service Set Identifier – это название
BSSID – Basic Service Set Identifier – MAC адрес сетевого адаптера (Wi-Fi точки)
level — Received Signal Strength Indicator
frequency — частота работы точки Wi-Fi [Гц]

capabilities - в строку записываются возможности точки
[WPA-PSK-TKIP+CCMP][WPA2-PSK-TKIP+CCMP][WPS][ESS]
[WPA2-PSK-CCMP][ESS]
[WPA2-PSK-CCMP+TKIP][ESS]
[WPA-PSK-CCMP+TKIP][WPA2-PSK-CCMP+TKIP][ESS]
[ESS][WPS]



 */

object Wifi {
    lateinit var c: Context

    val wifiManager = c.getSystemService(Context.WIFI_SERVICE) as WifiManager

    val wifiScanReceiver = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                scanSuccess()
            }
        }
    }

    fun f01() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        c.registerReceiver(wifiScanReceiver, intentFilter)
        val s = wifiManager.startScan()
    }

    private fun scanSuccess() {
        val results: List<ScanResult> = wifiManager.scanResults
    }

    /* по частоте определяем номер канала */
    fun channel(frequency: Int): Int {
        return if (frequency in 2412..2484) {
            (frequency - 2412) / 5 + 1
        } else if (frequency in 5170..5825) {
            (frequency - 5170) / 5 + 34
        } else {
            -1
        }
    }
}