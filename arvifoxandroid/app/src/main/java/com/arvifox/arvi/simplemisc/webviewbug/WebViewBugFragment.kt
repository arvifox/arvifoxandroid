package com.arvifox.arvi.simplemisc.webviewbug

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.arvifox.arvi.databinding.FragmentWebViewBugBinding
import com.arvifox.arvi.utils.FormatUtils.showToast

/**
 * A simple [Fragment] subclass.
 */
class WebViewBugFragment : Fragment() {

    companion object {
        fun newInstance(): WebViewBugFragment {
            return WebViewBugFragment()
        }
    }

    private var bi: FragmentWebViewBugBinding? = null
    private val binding by lazy { bi!! }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        bi = FragmentWebViewBugBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val se = binding.wvWeb.settings
        se.javaScriptEnabled = true
        se.domStorageEnabled = true
        binding.wvWeb.loadUrl("https://www.hibiny.com")

        binding.wvWeb.webViewClient = object : WebViewClient() {


            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                activity?.showToast("onPageFinished")
            }
        }
    }

}

class AppReceiver : BroadcastReceiver() {

    override fun onReceive(p0: Context?, p1: Intent?) {
        Log.d("foxx", p1?.action ?: "herr")
    }
}
