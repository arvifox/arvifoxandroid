package com.arvifox.arvi.simplemisc.anim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R
import com.arvifox.arvi.databinding.FragmentBottomNavAnimBinding

/**
 * A simple [Fragment] subclass.
 *
 * [https://blog.stylingandroid.com/bottomnavigationview-animating-icons/]
 */
class BottomNavAnimFragment : Fragment() {

    companion object {
        fun newInstance(): BottomNavAnimFragment {
            return BottomNavAnimFragment()
        }
    }

    private var bi: FragmentBottomNavAnimBinding? = null
    private val binding by lazy { bi!! }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bi = FragmentBottomNavAnimBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toggle.setOnCheckedChangeListener { _, b ->
            binding.bottomNavBar.menu.findItem(R.id.bnaItem1).icon = if (b) resources.getDrawable(
                R.drawable.loadingv2_ongoing_selector,
                activity?.theme
            ) else resources.getDrawable(R.drawable.loadingv2_vector, activity?.theme)
        }
    }
}
