package com.arvifox.arvi.simplemisc.anim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.databinding.FragmentExitAnimBinding
import com.arvifox.arvi.simplemisc.anim.ExitAnim.exitCircularReveal
import com.arvifox.arvi.simplemisc.anim.ExitAnim.startCircularReveal

class ExitAnimFragment : Fragment(), ExitAnim.ExitWithAnimation {

    companion object {
        fun newInstance(poss: IntArray): ExitAnimFragment {
            return ExitAnimFragment().apply {
                posX = poss[0]
                posY = poss[1]
            }
        }
    }

    private lateinit var binding: FragmentExitAnimBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentExitAnimBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(vi: View, savedInstanceState: Bundle?) {
        super.onViewCreated(vi, savedInstanceState)
        binding.exitanimfragm.setOnClickListener {
            view?.exitCircularReveal(posX!!, posY!!) {
                activity?.onBackPressed()
            }
        }
        view?.startCircularReveal(false)
    }

    override var posX: Int? = 0
    override var posY: Int? = 0

    override fun isExitAnimation(): Boolean = true
}
