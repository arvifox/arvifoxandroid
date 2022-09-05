package com.arvifox.arvi.simplemisc.anim.rev

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.drawable.AnimatedVectorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.fragment.app.Fragment
import com.arvifox.arvi.databinding.FragmentAteneBinding
import com.arvifox.arvi.utils.safeCast

class AteneFragment : Fragment() {

    private lateinit var binding: FragmentAteneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAteneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        ane()
        bane()
        traj()
    }

    fun traj() {
        binding.fova.postDelayed({
            binding.imagapa.drawable.safeCast<AnimatedVectorDrawable>()?.start()
        }, 1000)
    }

    fun bane() {
        binding.fova.animate().apply {
            duration = 2000
            startDelay = 600
        }.translationXBy(40f).start()
    }

    fun ane() {
        val d1 = ObjectAnimator.ofFloat(binding.fova, View.TRANSLATION_X, 0F, -30F, 70F).apply {
            duration = 2000
            startDelay = 700
            interpolator = LinearInterpolator()
        }
        val d2 = ObjectAnimator.ofFloat(binding.fova, View.SCALE_X, 2F).apply {
            duration = 2000
            startDelay = 1000
            interpolator = LinearInterpolator()
        }
        val s = AnimatorSet()
        s.playTogether(d1, d2)
        s.start()
    }

}