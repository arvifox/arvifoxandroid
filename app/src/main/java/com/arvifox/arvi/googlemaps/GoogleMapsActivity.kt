package com.arvifox.arvi.googlemaps

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.app_bar_layout.*

import com.arvifox.arvi.utils.FormatUtils.format

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_google_maps.*

class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private lateinit var mMarker: Marker

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, GoogleMapsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        val tb = toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.frGoogleMap) as SupportMapFragment
        mapFragment.getMapAsync(this)

        tvText1.setOnClickListener { _ ->
            mMarker = mMap.addMarker(MarkerOptions()
                    .position(LatLng(66.0, 33.0))
                    .title("Where")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_hibin))
                    .anchor(0f, 0.3f)
                    .rotation(30f)
                    .alpha(0.5f))
        }

        tvText2.setOnClickListener { _ -> mMarker.remove() }

        tvText3.setOnClickListener { _ ->
            val polylineOptions = PolylineOptions().add(LatLng(66.0, 33.0), LatLng(66.5, 33.5), LatLng(66.7, 33.1))
                    .color(Color.CYAN).width(1f)
            mMap.addPolyline(polylineOptions)
            val polygonOptions = PolygonOptions().add(LatLng(66.0, 33.0), LatLng(65.7, 33.8), LatLng(65.4, 32.1))
                    .strokeColor(Color.GREEN).strokeWidth(10f).fillColor(Color.MAGENTA)
            mMap.addPolygon(polygonOptions)
            val circleOptions = CircleOptions().center(LatLng(66.0, 33.0)).radius(100000.0)
            mMap.addCircle(circleOptions)

            val ov = GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.ic_hibin))
                    .position(LatLng(68.0, 33.0), 100000f, 100000f).transparency(0.5f)
            mMap.addGroundOverlay(ov)
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressWarnings("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(67.57, 33.4)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Apatity"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        mMap.setOnMapClickListener { latLng: LatLng? ->
            Toast.makeText(this, "onMapClick: " + latLng?.latitude?.format(2) + "," + latLng?.longitude, Toast.LENGTH_SHORT).show()
            val cameraPosition = CameraPosition.builder()
                    .target(LatLng(68.0, 34.0))
                    .zoom(5f)
                    .bearing(45f)
                    .tilt(20f)
                    .build()
            val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)
            mMap.animateCamera(cameraUpdate)
        }
        mMap.setOnMapLongClickListener { latLng: LatLng? -> Toast.makeText(this, "onMapLongClick: " + latLng?.latitude + "," + latLng?.longitude, Toast.LENGTH_SHORT).show() }
        mMap.setOnCameraChangeListener { cameraPosition: CameraPosition? -> Toast.makeText(this, "onCameraChange: " + cameraPosition?.zoom?.format(2), Toast.LENGTH_SHORT).show() }

        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        mMap.isIndoorEnabled = false
        mMap.isBuildingsEnabled = false
        mMap.isMyLocationEnabled = true
        val uiSettings = mMap.uiSettings
        uiSettings.isMyLocationButtonEnabled = true
        uiSettings.isCompassEnabled = true
        uiSettings.isZoomControlsEnabled = true
    }
}
