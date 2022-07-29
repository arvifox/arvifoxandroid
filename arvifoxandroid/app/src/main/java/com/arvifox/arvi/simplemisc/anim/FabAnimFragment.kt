package com.arvifox.arvi.simplemisc.anim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.databinding.FragmentFabAnimBinding

/**
 * A simple [Fragment] subclass.
 */
class FabAnimFragment : Fragment() {

    companion object {
        fun newInstance(): FabAnimFragment {
            return FabAnimFragment()
        }
    }

    private lateinit var binding: FragmentFabAnimBinding

    private var rotate = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentFabAnimBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fabAdd.setOnClickListener {
            rotate = ViewAnimation.rotateFab(it, !rotate)
            if (rotate) {
                ViewAnimation.showIn(binding.fabCall);
                ViewAnimation.showIn(binding.fabMic);
            } else {
                ViewAnimation.showOut(binding.fabCall);
                ViewAnimation.showOut(binding.fabMic);
            }
        }
        ViewAnimation.init(binding.fabCall)
        ViewAnimation.init(binding.fabMic)
    }

}
