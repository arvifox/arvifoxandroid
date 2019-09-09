package com.arvifox.arvi.geoposition

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.fragment_geo.*

class GeoFragment : Fragment() {

    lateinit var locationViewModel: LocationViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_geo, container, false)
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {
            tvGeoPosition.text =  "longitude=${it.longitude} / latitude=${it.latitude}"
        })
    }
}
