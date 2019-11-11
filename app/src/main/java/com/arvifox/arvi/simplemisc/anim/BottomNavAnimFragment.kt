package com.arvifox.arvi.simplemisc.anim


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.fragment_bottom_nav_anim.*

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_nav_anim, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toggle.setOnCheckedChangeListener { _, b ->
            bottom_nav_bar.menu.findItem(R.id.bnaItem1).icon = if (b) resources.getDrawable(
                R.drawable.loadingv2_ongoing_selector,
                activity?.theme
            ) else resources.getDrawable(R.drawable.loadingv2_vector, activity?.theme)
        }
    }
}
