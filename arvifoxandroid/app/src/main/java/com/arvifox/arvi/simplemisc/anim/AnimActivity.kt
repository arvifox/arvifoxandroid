package com.arvifox.arvi.simplemisc.anim

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.BounceInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityAnimBinding

class AnimActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, AnimActivity::class.java)
        }
    }

    private lateinit var binding: ActivityAnimBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val va = ValueAnimator.ofFloat(0f, 100f)
//        val va = AnimatorInflater.loadAnimator(this, R.animator.sometext) as ValueAnimator
        va.repeatCount = 2
        va.interpolator = AccelerateDecelerateInterpolator()
        va.duration = 700
        va.addUpdateListener { animation ->
            val pro = animation.animatedValue as Float
            binding.tvSome1.rotationX = pro
        }
        va.start()

        binding.btnTrans.setOnClickListener(object : View.OnClickListener {
            var vis: Boolean = false

            @SuppressLint("NewApi")
            override fun onClick(v: View?) {
                TransitionManager.beginDelayedTransition(binding.llTransition)
                vis = !vis
                binding.tvTrans.visibility = if (vis) View.VISIBLE else View.GONE
            }
        })
    }

    override fun onResume() {
        super.onResume()
        binding.btnSome.setOnClickListener {
            val buttonAnimator = ObjectAnimator.ofFloat(binding.btnSome, "translationX", 0f, 400f)
            buttonAnimator.duration = 3000
            buttonAnimator.interpolator = BounceInterpolator()
            buttonAnimator.start()
        }
    }
}
