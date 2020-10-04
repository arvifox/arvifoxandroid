package com.arvifox.arvi.simplemisc.camera

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.text.method.ScrollingMovementMethod
import android.view.Surface
import android.view.SurfaceHolder
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.arvifox.arvi.R
import com.arvifox.arvi.di.SimpleProvider
import com.arvifox.arvi.utils.FormatUtils.showToast
import com.arvifox.arvi.utils.Logger
import kotlinx.android.synthetic.main.activity_camera_shot.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CameraShotActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, CameraShotActivity::class.java)
        }
    }

    /**
     * Whether the current camera device supports Flash or not.
     */
    private var flashSupported = false

    private var backgroundThread: HandlerThread? = null
    private var backgroundHandler: Handler? = null

    private val uploadmapper = SimpleProvider.uploadImageApiMapper

    private lateinit var directory: File
    private lateinit var cameraManager: CameraManager
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var shc: SurfaceHolder.Callback
    private var cameraDevice: CameraDevice? = null
    private var cameraCaptureSession: CameraCaptureSession? = null

    private lateinit var sb: StringBuilder
    private var imageReader: ImageReader? = null

    private lateinit var lastPhoto: String

    private var was: Boolean = false
    private var he: Int = 0
    private var wi: Int = 0

    private var crb: CaptureRequest.Builder? = null
    private var irb: CaptureRequest.Builder? = null

    private var imgCapReq: CaptureRequest? = null

    @SuppressLint("NewApi", "MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_shot)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvCameraInfo.movementMethod = ScrollingMovementMethod()
        tvMiscInfo.movementMethod = ScrollingMovementMethod()
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
//        val sizes = scm.getOutputSizes(SurfaceHolder::class.java)
        val sizes = scm!!.getOutputSizes(ImageFormat.JPEG)
        sb.append("jpeg: ")
        for (sz in sizes) {
            sb.append("h=").append(sz.height).append(" w=").append(sz.width).append("\n")
        }
        he = sizes[0].height
        wi = sizes[0].width
        tvCameraInfo.text = sb.toString()

//        surfaceHolder = svFromCamera.holder
//        surfaceHolder.addCallback(shc)
        svFromCamera.holder.setFixedSize(240, 320)

        reader()
        btnGetCameraImage.setOnClickListener {
            cameraManager.openCamera("0", object : CameraDevice.StateCallback() {
                override fun onOpened(camera: CameraDevice) {
                    cameraDevice = camera
                    capture()
                }

                override fun onClosed(camera: CameraDevice) {
                    super.onClosed(camera)
                }

                override fun onDisconnected(camera: CameraDevice) {
                    cameraDevice?.close()
                    cameraDevice = null
                }

                override fun onError(camera: CameraDevice, error: Int) {
                    onDisconnected(camera)
                    this@CameraShotActivity.showToast("Camera Error")
                    finish()
                }
            }, backgroundHandler)
        }
        btnTakeImage.setOnClickListener {
            cameraCaptureSession?.capture(imgCapReq!!, object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureSequenceAborted(session: CameraCaptureSession, sequenceId: Int) {
                    super.onCaptureSequenceAborted(session, sequenceId)
                }

                override fun onCaptureCompleted(session: CameraCaptureSession, request: CaptureRequest, result: TotalCaptureResult) {
                    super.onCaptureCompleted(session, request, result)
//                    result?.partialResults?.get(0).
                }

                override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
                    super.onCaptureFailed(session, request, failure)
                }

                override fun onCaptureSequenceCompleted(session: CameraCaptureSession, sequenceId: Int, frameNumber: Long) {
                    super.onCaptureSequenceCompleted(session, sequenceId, frameNumber)
                }

                override fun onCaptureStarted(session: CameraCaptureSession, request: CaptureRequest, timestamp: Long, frameNumber: Long) {
                    super.onCaptureStarted(session, request, timestamp, frameNumber)
                }

                override fun onCaptureProgressed(session: CameraCaptureSession, request: CaptureRequest, partialResult: CaptureResult) {
                    super.onCaptureProgressed(session, request, partialResult)
                }

                override fun onCaptureBufferLost(session: CameraCaptureSession, request: CaptureRequest, target: Surface, frameNumber: Long) {
                    super.onCaptureBufferLost(session, request, target, frameNumber)
                }
            }, null)
        }

        btnUploadImage.setOnClickListener {
            val f = File(lastPhoto)
            val name = f.name.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val requestFile = f.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("image", f.name, requestFile)
            val call = uploadmapper.uploadImage(body, name)
            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Logger.e { t.localizedMessage }
                    showToast("Error while upload: ${t.localizedMessage}")
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    showToast(if (response.isSuccessful) "Upload successful" else "Upload res code = ${response.code()}")
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
    }

    @SuppressLint("NewApi", "MissingPermission")
    override fun onPause() {
        cameraCaptureSession?.close()
        cameraCaptureSession = null
        cameraDevice?.close()
        cameraDevice = null
        imageReader?.close()
        imageReader = null
        stopBackgroundThread()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 854 && resultCode == Activity.RESULT_OK && data != null) {
            val b = data.extras
            val bt = b!!.get("data")
            if (bt is Bitmap) {
                ivCameraResult.setImageBitmap(bt)
            }
        }
    }

    @SuppressLint("NewApi", "MissingPermission")
    private fun reader() {
        imageReader = ImageReader.newInstance(wi, he, ImageFormat.JPEG, 2).apply {
            setOnImageAvailableListener({
                backgroundHandler?.post(ImageSaverToFile(it.acquireLatestImage(), generateFile("photo")!!))
            }, backgroundHandler)
        }
    }

    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("CameraBackground").also { it.start() }
        backgroundHandler = Handler(backgroundThread?.looper!!)
    }

    @SuppressLint("NewApi")
    private fun stopBackgroundThread() {
        backgroundThread?.quitSafely()
        try {
            backgroundThread?.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: InterruptedException) {
            Logger.e { e.toString() }
        }

    }

    @SuppressLint("MissingPermission")
    private fun capture() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            crb = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            crb?.addTarget(svFromCamera.holder.surface)

            irb = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            irb?.addTarget(imageReader?.surface!!)
            irb?.set(CaptureRequest.CONTROL_CAPTURE_INTENT, CaptureRequest.CONTROL_CAPTURE_INTENT_STILL_CAPTURE)
//        irb?.set(CaptureRequest.CONTROL_AWB_MODE, CaptureRequest.CONTROL_AWB_MODE_INCANDESCENT)
//        irb?.set(CaptureRequest.CONTROL_EFFECT_MODE, CaptureRequest.CONTROL_EFFECT_MODE_SEPIA)
//        irb?.set(CaptureRequest.CONTROL_EFFECT_MODE, CameraMetadata.CONTROL_EFFECT_MODE_SEPIA)
            irb?.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_OFF)
            // focus
//        irb?.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_START)
            // exposure
            irb?.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_IDLE)

            imgCapReq = irb?.build()

            cameraDevice?.createCaptureSession(arrayListOf(svFromCamera.holder.surface, imageReader?.surface), object : CameraCaptureSession.StateCallback() {
                override fun onConfigureFailed(session: CameraCaptureSession) {
                    this@CameraShotActivity.showToast("session failed")
                }

                override fun onConfigured(session: CameraCaptureSession) {
                    this@CameraShotActivity.showToast("session configured well")
                    cameraCaptureSession = session
                    crb?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                    setAutoFlash(crb)
                    cameraCaptureSession?.setRepeatingRequest(crb?.build()!!, null, backgroundHandler)
                }
            }, null)
        }
    }

    /**
     * Lock the focus as the first step for a still image capture.
     */
    @SuppressLint("NewApi")
    private fun lockFocus() {
        try {
            // This is how to tell the camera to lock focus.
            crb?.set(CaptureRequest.CONTROL_AF_TRIGGER,
                    CameraMetadata.CONTROL_AF_TRIGGER_START)
            // Tell #captureCallback to wait for the lock.
//            state = STATE_WAITING_LOCK
//            cameraCaptureSession?.capture(crb?.build(), captureCallback,
//                    backgroundHandler)
        } catch (e: CameraAccessException) {
            Logger.e { e.toString() }
        }
    }

    @SuppressLint("NewApi")
    private fun setAutoFlash(requestBuilder: CaptureRequest.Builder?) {
        if (flashSupported) {
            requestBuilder?.set(CaptureRequest.CONTROL_AE_MODE,
                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
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

    private fun generateFile(type: String): File? {
        var file: File? = null
        when (type) {
            "photo" -> file = File(directory.getPath() + "/" + "photo_" + System.currentTimeMillis() + ".jpg")
            "video" -> file = File(directory.getPath() + "/" + "video_" + System.currentTimeMillis() + ".mp4")
        }
        file?.absolutePath?.apply { lastPhoto = this }
        return file
    }
}
