package com.arvifox.arvi.simplemisc.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.SurfaceHolder
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.activity_camera_shot.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import java.io.File


class CameraShotActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, CameraShotActivity::class.java)
        }
    }

    private lateinit var directory: File

    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var shc: SurfaceHolder.Callback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_shot)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnShot.setOnClickListener {
            // image
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // if we specify Extra_output, we'll get photo or video in the file, but not in the Intent
            // and vice versa
            i.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri("photo"))
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // video
//            val i = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
//            i.putExtra(MediaStore.EXTRA_OUTPUT, generateFileUri("video"))
            startActivityForResult(i, 854)
        }
        directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "MyFolder")
        directory.mkdir()

        surfaceHolder = svFromCamera.holder
        surfaceHolder.addCallback(shc)

        btnGetCameraImage.setOnClickListener {
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 854 && resultCode == Activity.RESULT_OK && data != null) {
            val b = data.extras
            val bt = b.get("data")
            if (bt is Bitmap) {
                ivCameraResult.setImageBitmap(bt)
            }
        }
    }

    /**
     * [Read about FileProvider](https://developer.android.com/reference/android/support/v4/content/FileProvider)
     */
    private fun generateFileUri(type: String): Uri {
        var file: File? = null
        when (type) {
            "photo" -> file = File(directory.getPath() + "/" + "photo_" + System.currentTimeMillis() + ".jpg")
            "video" -> file = File(directory.getPath() + "/" + "video_" + System.currentTimeMillis() + ".mp4")
        }
        return FileProvider.getUriForFile(this, getString(R.string.file_provider_authority), file!!)
//        return Uri.fromFile(file)
    }
}
