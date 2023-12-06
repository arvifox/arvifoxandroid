package com.arvifox.arvi.geoposition

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.arvifox.arvi.databinding.FragmentGeoBinding

class GeoFragment : Fragment() {

    lateinit var locationViewModel: LocationViewModel

    private lateinit var binding: FragmentGeoBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentGeoBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {
            binding.tvGeoPosition.text = "longitude=${it.longitude} / latitude=${it.latitude}"
        })
    }
}
