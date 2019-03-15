package com.arvifox.arvi.simplemisc.anim

import android.animation.AnimatorInflater
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import kotlinx.android.synthetic.main.activity_anim.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import android.view.animation.BounceInterpolator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.transition.TransitionManager
import android.view.View
import com.arvifox.arvi.R

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
//        val va = AnimatorInflater.loadAnimator(this, R.animator.sometext) as ValueAnimator
        va.repeatCount = 2
        va.interpolator = AccelerateDecelerateInterpolator()
        va.duration = 700
        va.addUpdateListener { animation ->
            val pro = animation.animatedValue as Float
            tvSome1.rotationX = pro
        }
        va.start()

        btnTrans.setOnClickListener(object : View.OnClickListener {
            var vis: Boolean = false
            @SuppressLint("NewApi")
            override fun onClick(v: View?) {
                TransitionManager.beginDelayedTransition(llTransition)
                vis = !vis
                tvTrans.visibility = if (vis) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        btnSome.setOnClickListener {
            val buttonAnimator = ObjectAnimator.ofFloat(btnSome, "translationX", 0f, 400f)
            buttonAnimator.duration = 3000
            buttonAnimator.interpolator = BounceInterpolator()
            buttonAnimator.start()
        }
    }
}
