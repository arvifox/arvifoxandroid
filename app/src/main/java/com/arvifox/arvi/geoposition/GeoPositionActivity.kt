package com.arvifox.arvi.geoposition

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.R
import com.arvifox.arvi.di.SimpleProvider
import kotlinx.android.synthetic.main.activity_geo_position.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import java.util.*


/**
 * geo
 *
 * desc
 *
 * @author arvifox
 * @since 27 07 2018
 */
class GeoPositionActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private val pm = SimpleProvider.permissionManager

    private val perms = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, GeoPositionActivity::class.java)
        }
    }

    /**
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_position)

        val tb = toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        btnLocationSettings.setOnClickListener { _ -> startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
    }

    @SuppressWarnings("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (pm.checkPermissions(this, *perms)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 10F, locationListener)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 10, 10F, locationListener)
        }
        checkEnabled()
    }

    override fun onPause() {
        locationManager.removeUpdates(locationListener)
        super.onPause()
    }

    private fun showLocation(location: Location?) {
        if (location == null) return
        if (location.provider == LocationManager.GPS_PROVIDER) {
            tvLocationGPS.text = formatLocation(location)
        } else if (location.provider == LocationManager.NETWORK_PROVIDER) {
            tvLocationNet.text = formatLocation(location)
        }
    }

    private fun formatLocation(location: Location?): String {
        return if (location == null) "" else String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3\$tF %3\$tT",
                location.latitude, location.longitude, Date(location.time))
    }

    private fun checkEnabled() {
        tvEnabledGPS.text = "Enabled: " + locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER)
        tvEnabledNet.text = "Enabled: " + locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location?) {
            showLocation(location)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            if (provider.equals(LocationManager.GPS_PROVIDER)) {
                tvStatusGPS.text = "Status: " + status
            } else if (provider.equals(LocationManager.NETWORK_PROVIDER)) {
                tvStatusNet.text = "Status: " + status
            }
        }

        @SuppressWarnings("MissingPermission")
        override fun onProviderEnabled(provider: String?) {
            checkEnabled()
            showLocation(locationManager.getLastKnownLocation(provider))
        }

        override fun onProviderDisabled(provider: String?) {
            checkEnabled()
        }
    }
}
