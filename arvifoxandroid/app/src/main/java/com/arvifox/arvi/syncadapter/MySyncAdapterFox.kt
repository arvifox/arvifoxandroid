package com.arvifox.arvi.syncadapter

import android.accounts.Account
import android.content.*
import android.os.Bundle

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 *
 * https://developer.android.com/training/sync-adapters/running-sync-adapter
 */
class MySyncAdapterFox @JvmOverloads constructor(
    context: Context,
    autoInitialize: Boolean,
    /**
     * Using a default argument along with @JvmOverloads
     * generates constructor for both method signatures to maintain compatibility
     * with Android 3.0 and later platform versions
     */
    allowParallelSyncs: Boolean = false,
    /*
     * If your app uses a content resolver, get an instance of it
     * from the incoming Context
     */
    val mContentResolver: ContentResolver = context.contentResolver
) : AbstractThreadedSyncAdapter(context, autoInitialize, allowParallelSyncs) {

    override fun onPerformSync(
        p0: Account?,
        p1: Bundle?,
        p2: String?,
        p3: ContentProviderClient?,
        p4: SyncResult?
    ) {

    }
}