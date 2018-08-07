package com.arvifox.arvi.simplemisc.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.app.AppCompatActivity
import android.view.Surface
import android.view.SurfaceHolder
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.activity_camera_shot.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import java.io.File


class CameraShotActivity : AppCompatActivity() {

    val ID = 0

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, CameraShotActivity::class.java)
        }
    }

    private lateinit var directory: File

    private var camera: Camera? = null
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

        shc = object : SurfaceHolder.Callback {
            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                camera?.stopPreview()
                setCameraDisplayOrientation(ID)
                camera?.setPreviewDisplay(holder)
                camera?.startPreview()
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
            }

            override fun surfaceCreated(holder: SurfaceHolder?) {
//                camera?.setPreviewDisplay(holder)
//                camera?.startPreview()
            }
        }
        surfaceHolder = svFromCamera.holder
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        surfaceHolder.addCallback(shc)

        btnGetCameraImage.setOnClickListener {
            camera = Camera.open(ID)
            setPreviewSize(false)
        }
    }

    override fun onPause() {
        camera?.release()
        camera = null
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

    private fun setCameraDisplayOrientation(cameraId: Int) {
        // определяем насколько повернут экран от нормального положения
        val rotation = windowManager.defaultDisplay.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result = 0
        // получаем инфо по камере cameraId
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)

        // задняя камера
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
            result = ((360 - degrees) + info.orientation)
        } else
        // передняя камера
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = ((360 - degrees) - info.orientation)
                result += 360
            }
        result = result % 360
        camera?.setDisplayOrientation(result)
    }

    private fun setPreviewSize(fullScreen: Boolean) {

        // получаем размеры экрана
        val display = windowManager.defaultDisplay
        val widthIsMax = display.width > display.height

        // определяем размеры превью камеры
        val size = camera?.parameters?.previewSize

        val rectDisplay = RectF()
        val rectPreview = RectF()

        // RectF экрана, соотвествует размерам экрана
        rectDisplay.set(0f, 0f, display.width.toFloat(), display.height.toFloat())

        // RectF первью
        if (widthIsMax) {
            // превью в горизонтальной ориентации
            rectPreview.set(0f, 0f, size?.width?.toFloat()!!, size.height.toFloat())
        } else {
            // превью в вертикальной ориентации
            rectPreview.set(0f, 0f, size?.height?.toFloat()!!, size.width.toFloat())
        }

        val matrix = Matrix()
        if (!fullScreen) {
            matrix.setRectToRect(rectPreview, rectDisplay,
                    Matrix.ScaleToFit.START)
        } else {
            matrix.setRectToRect(rectDisplay, rectPreview,
                    Matrix.ScaleToFit.START)
            matrix.invert(matrix)
        }
        matrix.mapRect(rectPreview)

        // установка размеров surface из получившегося преобразования
        svFromCamera.layoutParams.height = rectPreview.bottom.toInt()
        svFromCamera.layoutParams.width = rectPreview.right.toInt()
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
