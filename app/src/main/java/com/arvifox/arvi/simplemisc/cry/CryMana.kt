package com.arvifox.arvi.simplemisc.cry

import java.nio.ByteBuffer
import java.security.KeyPairGenerator
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.Security
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class CryMana {

    fun getProviders() = Security.getProviders().map {
        "${it.name} ${it.info} ${it.version}"
    }

    fun digest(_data: String): Pair<String, String> {
//        val md = MessageDigest.getInstance("SHA3-256")
        val md = MessageDigest.getInstance("SHA-256")
        val bqb = md.digest(_data.encodeToByteArray())
        return String(bqb) to bytesToHex(bqb)
    }

    fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }

    fun getRandomNonce(numBytes: Int): ByteArray {
        val nonce = ByteArray(numBytes)
        SecureRandom().nextBytes(nonce)
        return nonce
    }

    fun concatBytes(a: ByteArray, b: ByteArray): ByteArray =
        ByteBuffer.allocate(a.size + b.size).put(a).put(b).array()

    fun getKeySymmetric(): SecretKey {
        val kg = KeyGenerator.getInstance("AES")
        kg.init(256, SecureRandom())
        return kg.generateKey()
    }

    fun getKeyOpenkey(): String {
        val kg = KeyPairGenerator.getInstance("RSA")
//        val kg = KeyPairGenerator.getInstance("ECDSA")
        kg.initialize(1024)
        val kp = kg.generateKeyPair()
        val pu = kp.public
        val pr = kp.private
        return ""
    }

    fun encrypt001(t: String): ByteArray {
        val c = Cipher.getInstance("AES/CBC/PKCS5Padding")
        c.init(
            Cipher.ENCRYPT_MODE,
            SecretKeySpec(
                byteArrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15),
                "AES"
            ),
            IvParameterSpec(ByteArray(16))
        )
        return c.doFinal(t.encodeToByteArray())
    }

    fun decrypt001(key: ByteArray, encrypted: ByteArray): String {
        require(key.size == 16) { "Invalid key size." }
        val skeySpec = SecretKeySpec(key, "AES")
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(
            Cipher.DECRYPT_MODE, skeySpec,
            IvParameterSpec(ByteArray(16))
        )
        val original = cipher.doFinal(encrypted)
        return String(original)
    }
}