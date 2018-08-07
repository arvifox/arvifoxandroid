package com.arvifox.arvi.simplemisc.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.ImageReader
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.Surface
import android.view.SurfaceHolder
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.FormatUtils.tobitmap
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
    private lateinit var cameraManager: CameraManager
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var shc: SurfaceHolder.Callback
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null

    private lateinit var sb: StringBuilder
    private lateinit var imageReader: ImageReader

    private var was: Boolean = false

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_shot)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvCameraInfo.movementMethod = ScrollingMovementMethod()
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

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cIdList = cameraManager.cameraIdList
        sb = StringBuilder()
        for (s in cIdList) {
            sb.append("id=").append(s).append(", ")
        }
        sb.append("\n")
        val cc = cameraManager.getCameraCharacteristics("0")
        val scm = cc[CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]
        val sizes = scm.getOutputSizes(SurfaceHolder::class.java)
//        val sizes = scm.getOutputSizes(ImageFormat.JPEG)
        sb.append("jpeg: ")
        for (sz in sizes) {
            sb.append("h=").append(sz.height).append(" w=").append(sz.width).append("\n")
        }
        tvCameraInfo.text = sb.toString()

        surfaceHolder = svFromCamera.holder
//        surfaceHolder.addCallback(shc)

        reader()
        btnGetCameraImage.setOnClickListener {
            cameraManager.openCamera("0", object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice?) {
                    cameraDevice = camera!!
                    capture()
                }

                override fun onClosed(camera: CameraDevice?) {
                    super.onClosed(camera)
                }

                override fun onDisconnected(camera: CameraDevice?) {
                    cameraDevice?.close()
                    cameraDevice = null
                }

                override fun onError(camera: CameraDevice?, error: Int) {
                }
            }, null)
        }
    }

    @SuppressLint("NewApi", "MissingPermission")
    override fun onPause() {
        cameraDevice?.close()
        cameraDevice = null
        super.onPause()
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

    @SuppressLint("NewApi", "MissingPermission")
    private fun reader() {
        imageReader = ImageReader.newInstance(320, 240, ImageFormat.JPEG, 1)
        imageReader.setOnImageAvailableListener({ reader: ImageReader ->
            if (!was) {
                val im = reader.acquireLatestImage()
                was = true
                ivCameraResult.setImageBitmap(im.tobitmap())
            }
        }, null)
    }

    @SuppressLint("NewApi", "MissingPermission")
    private fun capture() {
        val crb = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        svFromCamera.holder.setFixedSize(240, 320)
        val surface = svFromCamera.holder.surface
        crb?.addTarget(surface)
        crb?.addTarget(imageReader.surface)
        cameraDevice?.createCaptureSession(arrayListOf(surface, imageReader.surface), object : CameraCaptureSession.StateCallback() {
            override fun onReady(session: CameraCaptureSession?) {
                super.onReady(session)
            }

            override fun onCaptureQueueEmpty(session: CameraCaptureSession?) {
                super.onCaptureQueueEmpty(session)
            }

            override fun onConfigureFailed(session: CameraCaptureSession?) {
            }

            override fun onClosed(session: CameraCaptureSession?) {
                super.onClosed(session)
            }

            override fun onSurfacePrepared(session: CameraCaptureSession?, surface: Surface?) {
                super.onSurfacePrepared(session, surface)
            }

            override fun onConfigured(session: CameraCaptureSession?) {
                cameraCaptureSession = session
                cameraCaptureSession?.setRepeatingRequest(crb?.build(), null, null)
            }

            override fun onActive(session: CameraCaptureSession?) {
                super.onActive(session)
            }
        }, null)
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
