package com.arvifox.arvi.syncadapter

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * A bound Service that instantiates the authenticator
 * when started.
 */
class StubAuthenService : Service() {

    // Instance field that stores the authenticator object
    private lateinit var mAuthenticator: StubAuthen

    override fun onCreate() {
        // Create a new authenticator object
        mAuthenticator = StubAuthen(this)
    }

    /*
     * When the system binds to this Service to make the RPC call
     * return the authenticator's IBinder.
     */
    override fun onBind(intent: Intent?): IBinder = mAuthenticator.iBinder
}