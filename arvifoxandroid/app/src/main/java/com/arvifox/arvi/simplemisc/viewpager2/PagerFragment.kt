package com.arvifox.arvi.simplemisc.viewpager2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.databinding.ItemViewpagerTestBinding

class PagerFragment : Fragment() {

    private lateinit var binding: ItemViewpagerTestBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = ItemViewpagerTestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.let {
            binding.ivViewPager.setBackgroundResource(it.getInt("color"))
            binding.tvTitle.text = "Item ${it.getInt("position")}"
        }
    }
}