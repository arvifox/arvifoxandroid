package com.arvifox.arvi.simplemisc.constrlayout

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.transition.TransitionManager
import com.arvifox.arvi.databinding.ActivityConstrLayoutBinding

class ConstrLayoutActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, ConstrLayoutActivity::class.java)
        }
    }

    private var showView: Boolean = false
    private lateinit var binding: ActivityConstrLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConstrLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnConstrdo.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.clConstLayout)
            binding.txtWorst.visibility = if (showView) View.VISIBLE else View.GONE
            showView = !showView
        }
    }
}
