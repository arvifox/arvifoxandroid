package com.arvifox.arvi.syncadapter

import android.content.BroadcastReceiver
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import com.google.android.gms.gcm.GoogleCloudMessaging

// Constants
// Content provider authority
const val AUTHORITY = "com.example.android.datasync.provider"
// Account type
const val ACCOUNT_TYPE = "com.example.android.datasync"
// Account
const val ACCOUNT = "default_account"
// Incoming Intent key for extended data
const val KEY_SYNC_REQUEST = "com.example.android.datasync.KEY_SYNC_REQUEST"

class GcmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // Get a GCM object instance
        val gcm: GoogleCloudMessaging = GoogleCloudMessaging.getInstance(context)
        // Get the type of GCM message
        val messageType: String? = gcm.getMessageType(intent)
        /*
         * Test the message type and examine the message contents.
         * Since GCM is a general-purpose messaging system, you
         * may receive normal messages that don't require a sync
         * adapter run.
         * The following code tests for a a boolean flag indicating
         * that the message is requesting a transfer from the device.
         */
        if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE == messageType
            && intent.getBooleanExtra(KEY_SYNC_REQUEST, false)
        ) {
            /*
             * Signal the framework to run your sync adapter. Assume that
             * app initialization has already created the account.
             */
            //ContentResolver.requestSync(mAccount, AUTHORITY, null)

        }

    }

}