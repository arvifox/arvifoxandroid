package com.arvifox.arvi.googlemaps

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.app_bar_layout.*

class GoogleMapsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        val tb = toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


    }
}
