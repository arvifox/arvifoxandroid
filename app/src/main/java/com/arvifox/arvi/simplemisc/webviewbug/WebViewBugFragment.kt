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
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.FormatUtils.showToast
import kotlinx.android.synthetic.main.fragment_web_view_bug.*

/**
 * A simple [Fragment] subclass.
 */
class WebViewBugFragment : Fragment() {

    companion object {
        fun newInstance(): WebViewBugFragment {
            return WebViewBugFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view_bug, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val se = wvWeb.settings
        se.javaScriptEnabled = true
        se.domStorageEnabled = true
        wvWeb.loadUrl("https://www.hibiny.com")

        wvWeb.webViewClient = object : WebViewClient() {


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
