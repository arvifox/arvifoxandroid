package com.arvifox.arvi.https

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityHttpsBinding
import java.net.InetAddress
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLSocket

class HttpsActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, HttpsActivity::class.java)
        }
    }

    private var bi: ActivityHttpsBinding? = null
    private val binding by lazy { bi!! }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = ActivityHttpsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val tb = binding.incHttp.toolbar
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
                handler.post {
                    fromThread(
                        TextUtils.join("\n", protocols),
                        TextUtils.join("\n", sup)
                    )
                }
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
                handler.post {
                    fromThreadTls(
                        TextUtils.join("\n", protocols),
                        TextUtils.join("\n", supp)
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { fromThreadTls("some", "fail") }
            }
        }).start()
    }

    private fun fromThread(enabled: String, supported: String) {
        binding.tvHttpsProtocols.text = enabled + "\n\n" + supported
    }

    private fun fromThreadTls(enabled: String, supported: String) {
        binding.tvHttpsTls.text = enabled + "\n\n" + supported
    }
}
