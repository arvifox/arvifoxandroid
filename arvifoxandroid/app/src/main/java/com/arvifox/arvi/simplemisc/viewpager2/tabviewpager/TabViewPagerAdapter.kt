package com.arvifox.arvi.simplemisc.viewpager2.tabviewpager

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class TabViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = TabViewPagerItemFragment().apply {
        arguments = bundleOf(
                "b1" to "text =",
                "b2" to position
        )
    }

    override fun getCount(): Int = 7
}