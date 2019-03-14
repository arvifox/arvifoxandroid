package com.arvifox.arvi.simplemisc.anim

import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.activity_anim.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class AnimActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, AnimActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anim)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val va = ValueAnimator.ofFloat(0f, 100f)
        va.repeatCount = 2
        va.interpolator = AccelerateDecelerateInterpolator()
        va.duration = 700
        va.addUpdateListener { animation ->
            val pro = animation.animatedValue as Float
            tvSome1.rotationX = pro
        }
        va.start()
    }
}
