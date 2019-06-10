package com.arvifox.arvi.simplemisc.viewpager2.tabviewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.item_tabviewpager.*

class TabViewPagerItemFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.item_tabviewpager, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let { tvSomeText1.text = it.getString("b1") + it.getInt("b2") }
    }
}