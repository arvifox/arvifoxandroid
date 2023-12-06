package com.arvifox.arvi.auth

import android.content.Context
import android.util.Base64
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object AuthUtils {

    fun base() {
        val cc = Base64.encode("abra".toByteArray(), Base64.NO_WRAP)
        val huc = URL("").openConnection()
        //val hs = HttpsURLConnection()
        huc.setRequestProperty("Authorization", "Basic $cc")
    }

    fun bio(context: Context) {
        val biometricManager = BiometricManager.from(context)
        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS){
            // TODO: show in-app settings, make authentication calls.
        }
    }

    private fun instanceOfBiometricPrompt(context: Context, fa: FragmentActivity): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(context)

        val callback = object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                //showMessage("$errorCode :: $errString")
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                //showMessage("Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                //showMessage("Authentication was successful")
            }
        }
        val biometricPrompt = BiometricPrompt(fa, executor, callback)
        return biometricPrompt
    }

    private fun getPromptInfo(): BiometricPrompt.PromptInfo {
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("My App's Authentication")
            .setSubtitle("Please login to get access")
            .setDescription("My App is using Android biometric authentication")
            .setDeviceCredentialAllowed(true)
            .build()
        return promptInfo
    }
}

//class AuthenticationDataRepository constructor(
//    private val firebaseAuth: FirebaseAuth
//) {
//
//    suspend fun authenticate(
//        email: String,
//        password: String
//    ): FirebaseUser? {
//        firebaseAuth.signInWithEmailAndPassword(
//            email, password
//        ).await()
//        return firebaseAuth.currentUser ?: throw FirebaseAuthException("", "")
//    }
//}