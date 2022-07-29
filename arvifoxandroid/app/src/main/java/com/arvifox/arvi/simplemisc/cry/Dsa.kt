package com.arvifox.arvi.simplemisc.cry

//import jp.co.soramitsu.crypto.ed25519.EdDSASecurityProvider
import net.i2p.crypto.eddsa.EdDSASecurityProvider
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.Security
import java.security.Signature

object Dsa {

    fun qwe() {
        Security.addProvider(EdDSASecurityProvider())

        val keyGen: KeyPairGenerator = KeyPairGenerator.getInstance("EdDSA/SHA3", "EdDSA")
        val keyFac: KeyFactory = KeyFactory.getInstance("EdDSA/SHA3", "EdDSA")
        val sgr: Signature = Signature.getInstance("EdDSA/SHA3", "EdDSA")
    }
}