package com.arvifox.arvi.simplemisc.viewpager2.tabviewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.databinding.ItemTabviewpagerBinding

class TabViewPagerItemFragment : Fragment() {

    private lateinit var binding: ItemTabviewpagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ItemTabviewpagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let { binding.tvSomeText1.text = it.getString("b1") + it.getInt("b2") }
    }
}