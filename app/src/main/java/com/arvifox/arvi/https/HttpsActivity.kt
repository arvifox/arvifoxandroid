package com.arvifox.arvi.https

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import android.text.TextUtils
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.activity_https.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import java.net.InetAddress
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocket

class HttpsActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, HttpsActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_https)

        val tb = toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val handler = Handler(Looper.getMainLooper())

        Thread(Runnable {
            try {
                val inetAddress = InetAddress.getByName("www.hibiny.com")
                val factory = HttpsURLConnection.getDefaultSSLSocketFactory()
                var socket: SSLSocket
                socket = factory.createSocket(inetAddress, 80) as SSLSocket
                val protocols = socket.enabledProtocols
                val sup = socket.supportedProtocols
                handler.post { fromThread(TextUtils.join("\n", protocols), TextUtils.join("\n", sup)) }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { fromThread("some", "fail") }
            }
        }).start()

        Thread(Runnable {
            try {
                val inetAddress = InetAddress.getByName("www.hibiny.com")
                var factory = HttpsURLConnection.getDefaultSSLSocketFactory()
                factory = TLSSocketFactory(factory, true)
                val socket = factory.createSocket(inetAddress, 80) as SSLSocket
                val protocols = socket.enabledProtocols
                val supp = socket.supportedProtocols
                handler.post { fromThreadTls(TextUtils.join("\n", protocols), TextUtils.join("\n", supp)) }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { fromThreadTls("some", "fail") }
            }
        }).start()
    }

    private fun fromThread(enabled: String, supported: String) {
        tvHttpsProtocols.text = enabled + "\n\n" + supported
    }

    private fun fromThreadTls(enabled: String, supported: String) {
        tvHttpsTls.text = enabled + "\n\n" + supported
    }
}
