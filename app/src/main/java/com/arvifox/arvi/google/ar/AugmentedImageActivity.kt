package com.arvifox.arvi.google.ar

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.Logger
import com.google.ar.core.AugmentedImageDatabase
import com.google.ar.core.Config
import com.google.ar.core.Session

import kotlinx.android.synthetic.main.app_bar_layout.*
import java.io.IOException

class AugmentedImageActivity : AppCompatActivity() {

    private lateinit var session: Session
    private lateinit var config: Config

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

        session = Session(this)
        config = Config(session)
        createImageDb(config)
    }

    private fun createImageDb(c: Config): Boolean {
        val aib: Bitmap? = loadImage()
        aib ?: return false
        val aid = AugmentedImageDatabase(session)
        aid.addImage("foo", aib)
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
}
