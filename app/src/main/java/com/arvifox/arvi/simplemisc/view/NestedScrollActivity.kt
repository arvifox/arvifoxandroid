package com.arvifox.arvi.simplemisc.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityNestedScrollBinding

class NestedScrollActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, NestedScrollActivity::class.java)
        }
    }

    private lateinit var binding: ActivityNestedScrollBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNestedScrollBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
