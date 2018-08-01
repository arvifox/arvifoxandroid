package com.arvifox.arvi.google.arcorevision.visiontest

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.activity_vision_api_test.*

class VisionApiTestActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, VisionApiTestActivity::class.java)
        }
    }

    private val am: AccountManager by lazy { AccountManager.get(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vision_api_test)

        val tb = toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onResume() {
        super.onResume()
        gettingToken()
    }

    fun hasToken(token: String) {
        etToken.setText(token, TextView.BufferType.EDITABLE)
    }

    private fun gettingToken() {
        val getOAuthToken = GetOAuthToken(this, am.getAccountsByType("com.google")[0],
                "oauth2:https://www.googleapis.com/auth/cloud-vision", 246)
        getOAuthToken.execute(null as Void?)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 246 && resultCode == Activity.RESULT_OK) {
            gettingToken()
        }
    }
}
