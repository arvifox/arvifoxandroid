package com.arvifox.arvi.simplemisc.misc2.fragment3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.fragment_3misc2.*

class Misc2Fragment3 : Fragment() {

    companion object {
        fun newInstance(): Misc2Fragment3 {
            return Misc2Fragment3()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_3misc2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        misc2Fr3Text.setOnClickListener {
            childFragmentManager.beginTransaction().replace(R.id.containerMisc2Fr3, Misc2Fragment31.newInstance(), "").commit()
        }
    }
}