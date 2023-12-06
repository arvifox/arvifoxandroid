package com.arvifox.arvi.https

import java.net.InetAddress
import java.net.Socket
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

/**
 * {@link https://developer.android.com/reference/javax/net/ssl/SSLSocket.html}
 */
class TLSSocketFactory() : SSLSocketFactory() {

    private var factory: SSLSocketFactory? = null
    private var tls12Enabled: Boolean = false

    constructor(ssf: SSLSocketFactory) : this() {
        factory = ssf
    }

    constructor(ssf: SSLSocketFactory, tls: Boolean) : this(ssf) {
        factory = ssf
        tls12Enabled = tls
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return factory!!.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return factory!!.supportedCipherSuites
    }

    override fun createSocket(s: Socket?, host: String?, port: Int, autoClose: Boolean): Socket {
        return enableTLSOnSocket(factory!!.createSocket(s, host, port, autoClose))
    }

    override fun createSocket(): Socket {
        return enableTLSOnSocket(factory!!.createSocket())
    }

    override fun createSocket(host: String?, port: Int): Socket {
        return enableTLSOnSocket(factory!!.createSocket(host, port))
    }

    override fun createSocket(host: String?, port: Int, localHost: InetAddress?, localPort: Int): Socket {
        return enableTLSOnSocket(factory!!.createSocket(host, port, localHost, localPort))
    }

    override fun createSocket(host: InetAddress?, port: Int): Socket {
        return enableTLSOnSocket(factory!!.createSocket(host, port))
    }

    override fun createSocket(address: InetAddress?, port: Int, localAddress: InetAddress?, localPort: Int): Socket {
        return enableTLSOnSocket(factory!!.createSocket(address, port, localAddress, localPort))
    }

    private fun enableTLSOnSocket(socket: Socket): Socket {
        if (socket != null && socket is SSLSocket && tls12Enabled) {
            socket.enabledProtocols = arrayOf("TLSv1.2")
        }
        return socket
    }
}