package com.arvifox.arvi.simplemisc.matcomp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.R
import com.arvifox.arvi.databinding.ActivityMatCompBinding
import com.arvifox.arvi.utils.FormatUtils.showToast

class MatCompActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, MatCompActivity::class.java)
        }
    }

    private lateinit var binding: ActivityMatCompBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMatCompBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.bottomAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.bottomAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.bottomAppBarMenuItem1 -> {
                    // Do something for menu item 1
                    true
                }
                R.id.bottomAppBarMenuItem2 -> {
                    // Do something for menu item 2
                    true
                }
                else -> false
            }
        }
        binding.bottomAppBar.setNavigationOnClickListener { showToast("navi clicked") }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            showToast("home clicked")
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
