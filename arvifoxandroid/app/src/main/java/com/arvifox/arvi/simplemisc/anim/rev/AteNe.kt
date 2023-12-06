package com.arvifox.arvi.simplemisc.anim.rev

import android.animation.ValueAnimator
import android.os.Build
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.annotation.RequiresApi
import kotlin.math.pow

object AteNe {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun test01() {
        val va = ValueAnimator.ofInt(0, 100).apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener {
                Log.d("foxx", "fr=${it.animatedFraction} va=${it.animatedValue}")
            }
            setCurrentFraction(4F)
            currentPlayTime = 500
        }
        va.start()
    }
}

class Fonte : Interpolator {
    override fun getInterpolation(input: Float): Float = ((input * 2f - 1f).pow(3) + 1) * 0.5f
}