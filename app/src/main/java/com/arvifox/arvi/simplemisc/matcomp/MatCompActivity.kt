package com.arvifox.arvi.simplemisc.matcomp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.FormatUtils
import com.arvifox.arvi.utils.FormatUtils.showToast
import kotlinx.android.synthetic.main.activity_mat_comp.*

class MatCompActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, MatCompActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mat_comp)
        setSupportActionBar(bottomAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        bottomAppBar.setOnMenuItemClickListener { item ->
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
        bottomAppBar.setNavigationOnClickListener { showToast("navi clicked") }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            showToast("home clicked")
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
