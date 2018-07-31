package com.arvifox.arvi.google.arcorevision

import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.FormatUtils.takeString
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_oauth_test.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL

class OAuthTestActivity : AppCompatActivity() {

    private lateinit var token: String

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, OAuthTestActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth_test)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        askToken()
    }

    private fun askToken() {
        val am = AccountManager.get(this)
        val opt = Bundle()
        val acc = am.getAccountsByType("com.google")[0]
        am.getAuthToken(acc, "Manage your tasks", opt, this, OnTokenAcquired(), handler)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 234) {
            if (resultCode == Activity.RESULT_OK) {
                askToken()
            }
        }
    }

    private val handler = Handler()

    private fun myToken() {
        tvOAuth.setText(token, TextView.BufferType.EDITABLE)
        val sin: Single<String> = Single.fromCallable {
            val url = URL("https://www.googleapis.com/tasks/v1/users/@me/lists?key=" + "api key")
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.addRequestProperty("client_id", "[client id OAuth 2.0 from google cloud console console.cloud.google.com]")
//            con.addRequestProperty("client_secret", "[no need for android]")
            con.addRequestProperty("Authorization", "OAuth " + token)
            con.addRequestProperty("X-Android-Package", "com.arvifox.arvi")
            con.addRequestProperty("X-Android-Cert", "[sha-1 fingerprint]")
            con.doInput = true
            con.connect()
            if (con.responseCode != HTTP_OK) {
                return@fromCallable "code=" + con.responseCode + " mes=" + con.responseMessage + " \nstr=" + con.errorStream.takeString()
            }
            val inp = con.inputStream
            val res: String = inp.takeString()
            inp.close()
            con.disconnect()
            return@fromCallable res
        }
        sin.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe { t1, t2 -> if (t2 != null) tvResult.text = "error" else tvResult.text = t1 }
    }

    inner class OnTokenAcquired : AccountManagerCallback<Bundle> {
        override fun run(future: AccountManagerFuture<Bundle>?) {
            var intent: Intent? = null
            if (future != null && future.result != null) {
                val ob = future.result.get(AccountManager.KEY_INTENT)
                if (ob != null) intent = ob as Intent
            }
            if (intent != null) {
                startActivityForResult(intent, 234)
                return
            }
            val b = future?.result
            token = b?.getString(AccountManager.KEY_AUTHTOKEN) ?: ""
            myToken()
        }
    }
}
