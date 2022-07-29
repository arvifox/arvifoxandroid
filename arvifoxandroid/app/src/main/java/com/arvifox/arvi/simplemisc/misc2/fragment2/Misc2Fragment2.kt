package com.arvifox.arvi.simplemisc.misc2.fragment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R

class Misc2Fragment2 : Fragment() {

    companion object {
        fun newInstance(): Misc2Fragment2 {
            return Misc2Fragment2()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_2misc2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val laypar = bottomViewToo.layoutParams as CoordinatorLayout.LayoutParams
//        laypar.behavior = ScrollViewBehavior(activity)
    }
}