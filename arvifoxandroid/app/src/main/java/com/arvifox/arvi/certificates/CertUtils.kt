package com.arvifox.arvi.certificates

import java.io.IOException
import java.security.KeyStore
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.util.*

/*
[https://stackoverflow.com/questions/4458046/listing-all-installed-certificates-on-android]
 */
/*
System/etc/security/cacerts.bks
data/misc/keystore
 */
object CertUtils {
    fun printInstalledCertificates() {
        try {
            val ks: KeyStore? = KeyStore.getInstance("AndroidCAStore")
            if (ks != null) {
                ks.load(null, null)
                val aliases: Enumeration<String> = ks.aliases()
                while (aliases.hasMoreElements()) {
                    val alias: String = aliases.nextElement() as String
                    val cert: java.security.cert.X509Certificate = ks.getCertificate(alias) as java.security.cert.X509Certificate
                    //To print System Certs only
                    if (cert.issuerDN.name.contains("system", true)) {
                        println(cert.issuerDN.name)
                    }
                    //To print User Certs only
                    if (cert.issuerDN.name.contains("user", true)) {
                        println(cert.issuerDN.name)
                    }
                    //To print all certs
                    println(cert.issuerDN.name)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: java.security.cert.CertificateException) {
            e.printStackTrace()
        }
    }
}