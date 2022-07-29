package com.arvifox.arvi.google.ar

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityAugmentedImageBinding
import com.arvifox.arvi.google.utils.FullScreenHelper
import com.arvifox.arvi.utils.Logger
import com.google.ar.core.*
import com.google.ar.sceneform.FrameTime
import java.io.IOException

class AugmentedImageActivity : AppCompatActivity() {

    private var arSession: Session? = null
    private lateinit var config: Config
    private var imageIndex: Int = 0

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, AugmentedImageActivity::class.java)
        }
    }

    private lateinit var binding: ActivityAugmentedImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAugmentedImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.inApBa.toolbar)
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
            binding.asvView.setupSession(arSession)
        }
        arSession?.resume()
        binding.asvView.resume()
    }

    override fun onPause() {
        if (arSession != null) {
            binding.asvView.pause()
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
        binding.asvView.scene.addOnUpdateListener { fr -> onUpdateFrame(fr) }
    }

    private fun onUpdateFrame(frameTime: FrameTime) {
        val frame = binding.asvView.arFrame!!
        val updatedAugmentedImages = frame.getUpdatedTrackables(AugmentedImage::class.java)

        for (augmentedImage in updatedAugmentedImages) {
            if (augmentedImage.trackingState == TrackingState.TRACKING) {
                // Check camera image matches our reference image
                if (augmentedImage.name == "foo") {
                    val node = AugmentedImageNode(this, "model.sfb")
                    node.setImageI(augmentedImage)
                    binding.asvView.scene.addChild(node)
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
