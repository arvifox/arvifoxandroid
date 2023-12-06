package com.arvifox.arvi.siteback

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.*
import androidx.core.app.NotificationCompat
import com.arvifox.arvi.BuildConfig
import com.arvifox.arvi.R
import com.arvifox.arvi.di.SimpleProvider
import com.arvifox.arvi.utils.BaseStorage
import okhttp3.*
import java.io.IOException
import java.util.*

class BackGeoService : Service() {

    companion object {
        fun start(c: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                c.startForegroundService(Intent(c, BackGeoService::class.java))
            } else {
                c.startService(Intent(c, BackGeoService::class.java))
            }
        }
    }

    private var res: String = ""
    private var ena: String = ""
    private var gsta: String = ""
    private var nsta: String = ""
    private var gloc: String = ""
    private var nloc: String = ""

    private lateinit var locationManager: LocationManager
    private val pm = SimpleProvider.permissionManager

    private val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    lateinit var han: Handler

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        han.postDelayed(task, 5000)
        begin()
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, BaseStorage.notificationChannelID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Hello")
                .setContentText("Family")
        startForeground(8764, builder.build())
        han = Handler(Looper.getMainLooper())
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private val task = Runnable {
        res = ena + ";" + gsta + ";" + nsta + ";" + gloc + ";" + nloc
        res = res.replace(" ", "", true)
        val client = OkHttpClient()
        val request = Request.Builder()
                .url(BuildConfig.ARVI_API_URL + "deviceloc.php?loc=" + res)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
            }
        })
        locationManager.removeUpdates(locationListener)
        stopSelf()
    }

    @SuppressWarnings("MissingPermission")
    private fun begin() {
        if (pm.checkPermissions(this, *perms)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10F, locationListener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10F, locationListener)
        }
        checkEnabled()
    }

    private fun checkEnabled() {
        ena = "ge:" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) +
                "ne:" + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun showLocation(location: Location?) {
        if (location == null) return
        if (location.provider == LocationManager.GPS_PROVIDER) {
            gloc = formatLocation(location)
        } else if (location.provider == LocationManager.NETWORK_PROVIDER) {
            nloc = formatLocation(location)
        }
    }

    private fun formatLocation(location: Location?): String {
        return if (location == null) "" else String.format(
                "lat=%1$.6f,lon=%2$.6f,time = %3\$tF %3\$tT",
                location.latitude, location.longitude, Date(location.time))
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            showLocation(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                gsta = "gsta:$status"
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                nsta = "nsta:$status"
            }
        }

        @SuppressWarnings("MissingPermission")
        override fun onProviderEnabled(provider: String) {
            checkEnabled()
            showLocation(locationManager.getLastKnownLocation(provider))
        }

        override fun onProviderDisabled(provider: String) {
            checkEnabled()
        }
    }
}