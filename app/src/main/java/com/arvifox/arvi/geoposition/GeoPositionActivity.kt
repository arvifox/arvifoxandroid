package com.arvifox.arvi.geoposition

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.app_bar_layout.*

/**
 * geo
 *
 * desc
 *
 * @author arvifox
 * @since 27 07 2018
 */
class GeoPositionActivity : AppCompatActivity() {

    /**
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geo_position)

        val tb = toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        
    }
}
