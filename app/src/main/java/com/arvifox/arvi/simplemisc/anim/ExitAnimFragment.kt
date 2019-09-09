package com.arvifox.arvi.simplemisc.anim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R
import com.arvifox.arvi.simplemisc.anim.ExitAnim.exitCircularReveal
import com.arvifox.arvi.simplemisc.anim.ExitAnim.startCircularReveal
import kotlinx.android.synthetic.main.fragment_exit_anim.*

class ExitAnimFragment : Fragment(), ExitAnim.ExitWithAnimation {

    companion object {
        fun newInstance(poss: IntArray): ExitAnimFragment {
            return ExitAnimFragment().apply {
                posX = poss[0]
                posY = poss[1]
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_exit_anim, container, false)
    }

    override fun onViewCreated(vi: View, savedInstanceState: Bundle?) {
        super.onViewCreated(vi, savedInstanceState)
        exitanimfragm.setOnClickListener {
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
