package com.arvifox.arvi.arcorevision.visiontest

import android.accounts.Account
import android.app.Activity
import android.os.AsyncTask
import com.arvifox.arvi.arcorevision.visiontest.VisionApiTestActivity
import com.google.android.gms.auth.GoogleAuthException
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.auth.UserRecoverableAuthException
import java.io.IOException

class GetOAuthToken internal constructor(internal var mActivity: Activity, internal var mAccount: Account, internal var mScope: String, internal var mRequestCode: Int) : AsyncTask<Void, Void, String>() {

    override fun doInBackground(vararg params: Void): String? {
        try {
            val token = fetchToken()
            if (token != null) {
//                (mActivity as VisionApiTestActivity).onTokenReceived(token)
                return token
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)

    }

    @Throws(IOException::class)
    protected fun fetchToken(): String? {
        var accessToken: String
        try {
            accessToken = GoogleAuthUtil.getToken(mActivity, mAccount, mScope)
            GoogleAuthUtil.clearToken(mActivity, accessToken)
            accessToken = GoogleAuthUtil.getToken(mActivity, mAccount, mScope)
            return accessToken
        } catch (userRecoverableException: UserRecoverableAuthException) {
            mActivity.startActivityForResult(userRecoverableException.intent, mRequestCode)
        } catch (fatalException: GoogleAuthException) {
            fatalException.printStackTrace()
        }

        return null
    }
}