package com.arvifox.arvi.simplemisc.servicehandler

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityServiceHandlerBinding
import com.google.android.material.snackbar.Snackbar

class ServiceHandlerActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, ServiceHandlerActivity::class.java)
        }
    }

    private var bi: ActivityServiceHandlerBinding? = null
    private val binding by lazy { bi!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = ActivityServiceHandlerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incServicehan.toolbar)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnStart.setOnClickListener { startService(FooService.startIntent(this)) }
    }

}
