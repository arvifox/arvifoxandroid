package com.arvifox.arvi.simplemisc.servicehandler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.arvifox.arvi.R

import kotlinx.android.synthetic.main.activity_service_handler.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class ServiceHandlerActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, ServiceHandlerActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_handler)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnStart.setOnClickListener { startService(FooService.startIntent(this)) }
    }

}
