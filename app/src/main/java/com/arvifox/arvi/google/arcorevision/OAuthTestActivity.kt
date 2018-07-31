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
import com.arvifox.arvi.R

import kotlinx.android.synthetic.main.activity_oauth_test.*
import kotlinx.android.synthetic.main.app_bar_layout.*

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
        tvOAuth.text = token
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
