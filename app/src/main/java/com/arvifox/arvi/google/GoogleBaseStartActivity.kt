package com.arvifox.arvi.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityArCoreVisionBinding
import com.arvifox.arvi.google.ar.StartArActivity
import com.arvifox.arvi.google.googleapi.AccountsActivity
import com.arvifox.arvi.google.googleapi.OAuthTestActivity
import com.arvifox.arvi.google.googleapi.visiontest.VisionApiTestActivity
import com.google.ar.core.ArCoreApk

class GoogleBaseStartActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, GoogleBaseStartActivity::class.java)
        }
    }

    private lateinit var binding: ActivityArCoreVisionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArCoreVisionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tb = binding.inclAppB.toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        binding.btnArCore1.setOnClickListener { _ ->
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

        binding.btnVisionTestStart.setOnClickListener {
            startActivity(
                VisionApiTestActivity.newIntent(
                    this
                )
            )
        }
        binding.btnAccounts.setOnClickListener { startActivity(AccountsActivity.newIntent(this)) }
        binding.btnOAuthTest.setOnClickListener { startActivity(OAuthTestActivity.newIntent(this)) }

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
        binding.btnStartArCore.isEnabled = arav.isSupported
        Toast.makeText(this, "ArCore is not supported", Toast.LENGTH_SHORT).show()
        binding.btnStartArCore.setOnClickListener { startActivity(StartArActivity.newIntent(this)) }
    }
}
