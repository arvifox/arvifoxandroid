package com.arvifox.arvi.google.googleapi

import android.accounts.AccountManager
import android.accounts.AccountManagerCallback
import android.accounts.AccountManagerFuture
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityOauthTestBinding
import com.arvifox.arvi.utils.FormatUtils.takeString
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    private var bindingNull: ActivityOauthTestBinding? = null
    private val binding by lazy { bindingNull!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingNull = ActivityOauthTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incOauth.toolbar)

        binding.fab.setOnClickListener { view ->
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

    private suspend fun myToken() {
        binding.tvOAuth.setText(token, TextView.BufferType.EDITABLE)
        val sin = {
            val url = URL("https://www.googleapis.com/tasks/v1/users/@me/lists?key=" + "api key")
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.addRequestProperty(
                "client_id",
                "[client id OAuth 2.0 from google cloud console console.cloud.google.com]"
            )
//            con.addRequestProperty("client_secret", "[no need for android]")
            con.addRequestProperty("Authorization", "OAuth " + token)
            con.addRequestProperty("X-Android-Package", "com.arvifox.arvi")
            con.addRequestProperty("X-Android-Cert", "[sha-1 fingerprint]")
            con.doInput = true
            con.connect()
            if (con.responseCode != HTTP_OK) {
                "code=" + con.responseCode + " mes=" + con.responseMessage + " \nstr=" + con.errorStream.takeString()
            } else {
                val inp = con.inputStream
                val res: String = inp.takeString()
                inp.close()
                con.disconnect()
                res
            }
        }
        sin.invoke().let {
            binding.tvResult.text = it
        }
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
            GlobalScope.launch {
                myToken()
            }
        }
    }
}
