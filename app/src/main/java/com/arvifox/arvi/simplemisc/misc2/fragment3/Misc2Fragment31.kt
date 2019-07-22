package com.arvifox.arvi.simplemisc.misc2.fragment3

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R

class Misc2Fragment31 : Fragment() {

    companion object {
        fun newInstance(): Misc2Fragment31 {
            return Misc2Fragment31()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_3_1misc2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        scrollcoordinator.scrollableView = scrollview
    }
}