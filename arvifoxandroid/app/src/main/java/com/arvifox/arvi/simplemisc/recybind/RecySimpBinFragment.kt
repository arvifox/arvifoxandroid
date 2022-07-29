package com.arvifox.arvi.simplemisc.recybind

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.BR
import com.arvifox.arvi.R
import com.arvifox.arvi.databinding.FragmentRecySimpBinBinding
import com.arvifox.arvi.utils.FormatUtils.showToast

class RecySimpBinFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bin = FragmentRecySimpBinBinding.inflate(inflater, container, false)
        bin.rvSimpleBind.setHasFixedSize(true)
        bin.rvSimpleBind.adapter = SimpleAdapter(
            R.layout.item_simple_recy_bin,
            listOf(
                RecySimpleData("", "")
            ),
            BR.varRecyItemSimple,
            BR.varSimpleOnItemClick
        ) { v, i, p ->
            activity?.showToast("${v.id} - $i - $p")
        }

        return bin.root
    }
}

data class RecySimpleData(val t: String, val t2: String)