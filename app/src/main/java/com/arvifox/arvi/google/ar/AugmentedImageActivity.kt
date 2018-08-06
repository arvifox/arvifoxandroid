package com.arvifox.arvi.google.ar

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arvifox.arvi.R
import com.arvifox.arvi.google.utils.FullScreenHelper
import com.arvifox.arvi.google.utils.SnackbarHelper
import com.arvifox.arvi.utils.Logger
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session
import com.google.ar.sceneform.FrameTime
import kotlinx.android.synthetic.main.activity_augmented_image.*

import kotlinx.android.synthetic.main.app_bar_layout.*
import java.io.IOException
import com.google.ar.core.TrackingState
import com.google.ar.core.AugmentedImage

class AugmentedImageActivity : AppCompatActivity() {

    private var arSession: Session? = null
    private lateinit var config: Config
    private var imageIndex: Int = 0

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, AugmentedImageActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_augmented_image)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initializeSceneView()
    }

    override fun onResume() {
        super.onResume()
        if (arSession == null) {
            arSession = Session(this)
            config = Config(arSession)
            createImageDb(config)
            config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
            arSession?.configure(config)
            asvView.setupSession(arSession)
        }
        arSession?.resume()
        asvView.resume()
    }

    override fun onPause() {
        if (arSession != null) {
            asvView.pause()
            arSession?.pause()
        }
        super.onPause()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }

    //region private

    private fun initializeSceneView() {
        asvView.scene.addOnUpdateListener { fr -> onUpdateFrame(fr) }
    }

    private fun onUpdateFrame(frameTime: FrameTime) {
        val frame = asvView.arFrame
        val updatedAugmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)

        for (augmentedImage in updatedAugmentedImages) {
            if (augmentedImage.trackingState == TrackingState.TRACKING) {
                // Check camera image matches our reference image
                if (augmentedImage.name == "foo") {
                    val node = AugmentedImageNode(this, "model.sfb")
                    node.setImageI(augmentedImage)
                    asvView.scene.addChild(node)
                }

            }
        }
    }

    private fun createImageDb(c: Config): Boolean {
        val aib: Bitmap? = loadImage()
        aib ?: return false
        val aid = AugmentedImageDatabase(arSession)
        imageIndex = aid.addImage("foo", aib)
        c.augmentedImageDatabase = aid
        return true
    }

    private fun loadImage(): Bitmap? {
        try {
            val ist = assets.open("footballer.png")
            return BitmapFactory.decodeStream(ist)
        } catch (e: IOException) {
            e.printStackTrace()
            Logger.d { "sd" }
        }
        return null
    }

    //endregion
}
