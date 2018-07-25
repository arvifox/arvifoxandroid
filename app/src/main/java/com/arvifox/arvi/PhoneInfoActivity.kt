package com.arvifox.arvi

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import kotlinx.android.synthetic.main.activity_phone_info.*

class PhoneInfoActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, PhoneInfoActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_info)
        tvPhoneInfo.text = composeProperties()
    }

    private fun composeProperties(): String {
        val sb = StringBuilder()
        sb.append("Manufacturer = ").append(Build.MANUFACTURER).append("\n")
                .append("Model = ").append(Build.MODEL).append("\n")
                .append("Version = ").append(Build.VERSION.RELEASE).append("\n")
                .append("API Level = ").append(Build.VERSION.SDK_INT).append("\n")
                .append("Architecture = ").append(Build.CPU_ABI).append("\n")
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        sb.append("Density = ").append(dm.density).append("\n")
                .append("Resolution = (").append(dm.widthPixels / dm.density).append("dp, ")
                .append(dm.heightPixels / dm.density).append("dp)\n")
        return sb.toString()
    }
}
