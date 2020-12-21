package com.arvifox.arvi.simplemisc.cry

import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.security.*
import java.security.cert.CertPath
import java.security.cert.Certificate
import java.security.cert.CertificateException
import java.security.cert.CertificateFactory
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.Mac
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

    /**
     * MAC(Message Authentication Code)
     */
    fun getMac(): String {
        val mac: Mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(byteArrayOf(), ""))
        mac.update(byteArrayOf())
        val res = mac.doFinal(byteArrayOf())
        return bytesToHex(res)
    }

    /**
     *
     */
    fun getSignature(kp: KeyPair): String {
        val signature: Signature = Signature.getInstance("SHA256WithDSA")
        signature.initSign(kp.private)
        val data = "abcdefghijklmnopqrstuvxyz".toByteArray(charset("UTF-8"))
        signature.update(data)
        val digitalSignature = signature.sign()
        return ""
    }

    fun verifySignature(kp: KeyPair, digitalSignature: ByteArray): Boolean {
        val signature = Signature.getInstance("SHA256WithDSA")
        signature.initVerify(kp.public)
        val data2 = "abcdefghijklmnopqrstuvxyz".toByteArray(charset("UTF-8"))
        signature.update(data2)
        return signature.verify(digitalSignature)
    }

    fun keyStore(kp: KeyPair, sk: SecretKey) {
        //val ks = KeyStore.getInstance(KeyStore.getDefaultType())
        val ks = KeyStore.getInstance("PKCS12")
        ks.load(FileInputStream("file.ext"), charArrayOf('\u7654'))
        val en = ks.getEntry("alias", KeyStore.PasswordProtection("aliaspassword".toCharArray()))
        if (en is KeyStore.PrivateKeyEntry) {
            val prk = en.privateKey
            val cer = en.certificate
            val cech = en.certificateChain
        }

        ks.setEntry(
            "keyAlias",
            KeyStore.SecretKeyEntry(sk),
            KeyStore.PasswordProtection("".toCharArray())
        )

        FileOutputStream("file.ext").use {
            ks.store(it, "qwe".toCharArray())
        }
    }

    fun certs(ce: Certificate, pu: PublicKey) {
        val encoded = ce.encoded
        val pk = ce.publicKey
        val type: String = ce.type

        try {
            ce.verify(pu)

        } catch (e: InvalidKeyException) {
            // сертификат не был подписан данным открытым ключом
        } catch (e: NoSuchAlgorithmException) {
        } catch (e: NoSuchProviderException) {
        } catch (e: SignatureException) {
        } catch (e: CertificateException) {
        }

        val certificateFactory: CertificateFactory = CertificateFactory.getInstance("X.509")
        val certificateInputStream = FileInputStream("my-x509-certificate.crt")
        val certificate = certificateFactory.generateCertificate(certificateInputStream)

        val certificateInputStream2 = FileInputStream("my-x509-certificate-chain.crt")
        val certPath: CertPath = certificateFactory.generateCertPath(certificateInputStream2)
        val certificates = certPath.certificates

    }
}