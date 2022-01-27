package com.arvifox.arvi.simplemisc.cry

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arvifox.arvi.databinding.FragmentCryBinding

class CryFragment : Fragment() {

    val cryMana: CryMana by lazy { CryMana() }

    private lateinit var crypt001: ByteArray

    companion object {
        fun getInstance() = CryFragment()
    }

    private lateinit var binding: FragmentCryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentCryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner
        super.onViewCreated(view, savedInstanceState)
        binding.tvProvidersList.text = cryMana.getProviders().joinToString(separator = "\n")
        binding.tvDigest1.text = cryMana.digest("first string to digest").run { "$first\n$second" }
        binding.tvEncrypted.text = cryMana.runCatching {
            crypt001 = encrypt001("bit text to encrypt test fun")
            cryMana.bytesToHex(crypt001)
        }.getOrElse { it.message }
        binding.tvDecrypted.text = cryMana.runCatching {
            decrypt001(
                byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
                crypt001
            )
        }.getOrElse { it.message }
    }
}