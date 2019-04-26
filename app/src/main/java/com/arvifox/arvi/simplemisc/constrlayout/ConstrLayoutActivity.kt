package com.arvifox.arvi.simplemisc.constrlayout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionManager
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.activity_constr_layout.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class ConstrLayoutActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, ConstrLayoutActivity::class.java)
        }
    }

    private var showView: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_constr_layout)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnConstrdo.setOnClickListener {
            TransitionManager.beginDelayedTransition(clConstLayout)
            txt_worst.visibility = if (showView) View.VISIBLE else View.GONE
            showView = !showView
        }
    }
}
