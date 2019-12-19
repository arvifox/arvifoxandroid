package com.arvifox.arvi.domain.arccom

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

object Arcco1 {

    class Fra : Fragment() {
        val vm by viewModels<Mvm> { SavedStateViewModelFactory(this.activity!!.application, this) }

        //val s: ViewModel by activityViewModels<Mvm> {ViewModelProvider.Factory}

        val vvv = ViewModelProvider(
            this,
            SavedStateViewModelFactory(this.activity!!.application, this)
        ).get(Mvm::class.java)
    }

    class Mvm(ssh: SavedStateHandle) : ViewModel() {
        private val savedStateHandle = ssh
    }
}