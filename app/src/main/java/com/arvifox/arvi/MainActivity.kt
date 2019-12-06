package com.arvifox.arvi

import android.accounts.Account
import android.accounts.AccountManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.arvifox.arvi.domain.corou.Arv10
import com.arvifox.arvi.geoposition.GeoPositionActivity
import com.arvifox.arvi.google.GoogleBaseStartActivity
import com.arvifox.arvi.googlemaps.GoogleMapsActivity
import com.arvifox.arvi.https.HttpsActivity
import com.arvifox.arvi.navig.NavigActivity
import com.arvifox.arvi.simplemisc.SimpleMisc2Activity
import com.arvifox.arvi.simplemisc.SimpleMiscActivity
import com.arvifox.arvi.simplemisc.phoneinfo.PhoneInfoActivity
import com.arvifox.arvi.siteback.BackUtils
import com.arvifox.arvi.utils.BaseStorage
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*


// Constants
// The authority for the sync adapter's content provider
const val AUTHORITY = "com.arvifox.arvi.provider"
// An account type, in the form of a domain name
const val ACCOUNT_TYPE = "arvifox.com"
// The account name
const val ACCOUNT = "arvifoxAccount"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fa: FirebaseAnalytics

    // Instance fields
    private lateinit var mAccount: Account

    /**
     * Create a new dummy account for the sync adapter
     */
    private fun createSyncAccount(): Account {
        val accountManager = getSystemService(Context.ACCOUNT_SERVICE) as AccountManager
        return Account(ACCOUNT, ACCOUNT_TYPE).also { newAccount ->
            /*
             * Add the account and account type, no password or user data
             * If successful, return the Account object, otherwise report an error.
             */
            if (accountManager.addAccountExplicitly(newAccount, null, null)) {
                /*
                 * If you don't set android:syncable="true" in
                 * in your <provider> element in the manifest,
                 * then call context.setIsSyncable(account, AUTHORITY, 1)
                 * here.
                 */
            } else {
                /*
                 * The account exists or some other error occurred. Log this, report it,
                 * or handle it internally.
                 */
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Create the dummy account
        mAccount = createSyncAccount()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        btnMainButton01.setOnClickListener {
            Arv10.getWW()
        }

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        fa = FirebaseAnalytics.getInstance(this)

        if (!BaseStorage.isTokenSent(this)) {
            FirebaseInstanceId.getInstance().instanceId
                .addOnSuccessListener(this) { ins ->
                    Toast.makeText(this, ins.token, Toast.LENGTH_SHORT).show()
                    BackUtils.sendToken(ins.token, this)
                }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                BaseStorage.notificationChannelID,
                "Arvifox channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Arvifox channel"
            channel.enableLights(false)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            try {
                nm.createNotificationChannel(channel)
            } catch (ex: NullPointerException) {
                ex.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val rc = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if (rc != ConnectionResult.SUCCESS) {
            val d = GoogleApiAvailability.getInstance().getErrorDialog(this, rc, 1234)
            d.show()
        }

        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED)
        intentFilter.addAction(Intent.ACTION_PACKAGES_SUSPENDED)
        intentFilter.addAction(Intent.ACTION_PACKAGES_UNSUSPENDED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED)
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED)
        intentFilter.addDataScheme("package")
        registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                Log.d("foxx ff", "${p1?.action} and ${p1?.data}")
            }
        }, intentFilter)
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
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
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
            R.id.nav_simple_misc -> {
                startActivity(SimpleMiscActivity.newIntent(this))
            }
            R.id.nav_simple_misc2 -> {
                startActivity(SimpleMisc2Activity.newIntent(this))
            }
            R.id.nav_navig -> {
                startActivity(NavigActivity.newIntent(this))
            }
            R.id.nav_https -> {
                startActivity(HttpsActivity.newIntent(this))
            }
            R.id.nav_geo_posiiton -> {
                startActivity(GeoPositionActivity.newIntent(this))
            }
            R.id.nav_google_map -> {
                startActivity(GoogleMapsActivity.newIntent(this))
            }
            R.id.nav_google_ar -> {
                startActivity(GoogleBaseStartActivity.newIntent(this))
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

    override fun onDestroy() {
        Log.d("foxx", "main act on destroy")
        super.onDestroy()
    }
}
