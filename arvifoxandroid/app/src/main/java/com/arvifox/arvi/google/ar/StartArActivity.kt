package com.arvifox.arvi.google.ar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityStartArBinding
import com.google.ar.core.ArCoreApk
import com.google.ar.core.Session
import com.google.ar.core.exceptions.UnavailableUserDeclinedInstallationException

class StartArActivity : AppCompatActivity() {

    // Set to true ensures requestInstall() triggers installation if necessary.
    private var mUserRequestedInstall = true

    private var mSession: Session? = null

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, StartArActivity::class.java)
        }
    }

    private lateinit var binding: ActivityStartArBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartArBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incluAp.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnStartHelloSceneform.setOnClickListener {
            startActivity(
                HelloSceneActivity.newIntent(
                    this
                )
            )
        }
        binding.btnStartAugmImage.setOnClickListener {
            startActivity(
                AugmentedImageActivity.newIntent(
                    this
                )
            )
        }
    }

    /**
     * Note: If you are using Sceneform, ArFragment automatically performs these checks and creates the session for you.
     */
    override fun onResume() {
        super.onResume()
        try {
            if (mSession == null) {
                when (ArCoreApk.getInstance().requestInstall(this, mUserRequestedInstall)) {
                    ArCoreApk.InstallStatus.INSTALLED -> {
                        // Success.
                        mSession = Session(this)
                    }
                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                        // Ensures next invocation of requestInstall() will either return
                        // INSTALLED or throw an exception.
                        mUserRequestedInstall = false
                        return
                    }
                }
            }
        } catch (e: UnavailableUserDeclinedInstallationException) {
            // Display an appropriate message to the user and return gracefully.
            return
        } catch (e: Exception) {  // current catch statements

            return  // mSession is still null
        }
    }

}
