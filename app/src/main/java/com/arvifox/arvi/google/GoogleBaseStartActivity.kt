package com.arvifox.arvi.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.arvifox.arvi.R
import com.arvifox.arvi.google.googleapi.AccountsActivity
import com.arvifox.arvi.google.googleapi.OAuthTestActivity
import com.arvifox.arvi.google.googleapi.visiontest.VisionApiTestActivity
import com.google.ar.core.ArCoreApk
import kotlinx.android.synthetic.main.activity_ar_core_vision.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class GoogleBaseStartActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, GoogleBaseStartActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_core_vision)

        val tb = toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        btnArCore1.setOnClickListener { _ ->
            try {
                val intent = Intent("com.google.zxing.client.android.SCAN")
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE") // "PRODUCT_MODE for bar codes
                startActivityForResult(intent, 543)
            } catch (e: Exception) {
                val marketUri = Uri.parse("market://details?id=com.google.zxing.client.android")
                val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                startActivity(marketIntent)
            }
        }

        btnVisionTestStart.setOnClickListener { startActivity(VisionApiTestActivity.newIntent(this)) }
        btnAccounts.setOnClickListener { startActivity(AccountsActivity.newIntent(this)) }
        btnOAuthTest.setOnClickListener { startActivity(OAuthTestActivity.newIntent(this)) }

        checkArEnable()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 543) {
            if (resultCode == Activity.RESULT_OK) {
                val content = data?.getStringExtra("SCAN_RESULT")
                Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkArEnable() {
        val arav = ArCoreApk.getInstance().checkAvailability(this)
        if (arav.isTransient) Handler().postDelayed({ checkArEnable() }, 200)
        btnStartArCore.isEnabled = arav.isSupported
        btnStartArCore.setOnClickListener {  }
    }
}
