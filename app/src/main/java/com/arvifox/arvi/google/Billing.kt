package com.arvifox.arvi.google

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingResult

class Billing {

    private var billingClient: BillingClient? = null

    private var cont: Context? = null

    private fun bil() {
        billingClient = BillingClient.newBuilder(cont!!).setListener { responseCode, purchases ->
//            if (responseCode == ) { }
        }.build()

        billingClient?.startConnection(object : BillingClientStateListener {
            override fun onBillingServiceDisconnected() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onBillingSetupFinished(p0: BillingResult?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }
}