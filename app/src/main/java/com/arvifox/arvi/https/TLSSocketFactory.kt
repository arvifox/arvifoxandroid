package com.arvifox.arvi.https

import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

/**
 * {@link https://developer.android.com/reference/javax/net/ssl/SSLSocket.html}
 */
class TLSSocketFactory(val ssf: SSLSocketFactory) : SSLSocketFactory() {

    private var tls12Enabled: Boolean = false

    override fun getDefaultCipherSuites(): Array<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getSupportedCipherSuites(): Array<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSocket(): Socket {
        return super.createSocket()
    }

    override fun createSocket(host: String?, port: Int): Socket {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun enableTLSOnSocket(socket: Socket): Socket {
        if (socket != null && socket is SSLSocket && tls12Enabled) {
            (socket as SSLSocket).enabledProtocols = arrayOf("TLSv1.2")
        }
        return socket
    }
}