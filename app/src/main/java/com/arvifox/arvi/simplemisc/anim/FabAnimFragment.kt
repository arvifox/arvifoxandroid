package com.arvifox.arvi.simplemisc.anim

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.fragment_fab_anim.*

/**
 * A simple [Fragment] subclass.
 */
class FabAnimFragment : Fragment() {

    companion object {
        fun newInstance(): FabAnimFragment {
            return FabAnimFragment()
        }
    }

    private var rotate = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fab_anim, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fabAdd.setOnClickListener {
            rotate = ViewAnimation.rotateFab(it, !rotate)
            if(rotate){
                ViewAnimation.showIn(fabCall);
                ViewAnimation.showIn(fabMic);
            }else{
                ViewAnimation.showOut(fabCall);
                ViewAnimation.showOut(fabMic);
            }
        }
        ViewAnimation.init(fabCall)
        ViewAnimation.init(fabMic)
    }

}
