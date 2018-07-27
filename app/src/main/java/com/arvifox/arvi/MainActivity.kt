package com.arvifox.arvi

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.arvifox.arvi.geoposition.GeoPositionActivity
import com.arvifox.arvi.https.HttpsActivity
import com.arvifox.arvi.phoneinfo.PhoneInfoActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fa: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fa = FirebaseAnalytics.getInstance(this)

        FirebaseInstanceId.getInstance().instanceId
                .addOnSuccessListener(this, { ins ->
                    Toast.makeText(this, ins.token, Toast.LENGTH_SHORT).show()
                })
    }

    override fun onResume() {
        super.onResume()
        val rc = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (rc != ConnectionResult.SUCCESS) {
            val d = GoogleApiAvailability.getInstance().getErrorDialog(this, rc, 1234)
            d.show()
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_phoneinfo -> {
                val b = Bundle()
                b.putString(FirebaseAnalytics.Param.ITEM_ID, "phoneinfo")
                b.putString(FirebaseAnalytics.Param.ITEM_NAME, "nav drawer")
                b.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "click")
                fa.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, b)
                startActivity(PhoneInfoActivity.newIntent(this))
            }
            R.id.nav_https -> {
                startActivity(HttpsActivity.newIntent(this))
            }
            R.id.nav_geo_posiiton -> {
                startActivity(GeoPositionActivity.newIntent(this))
            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
