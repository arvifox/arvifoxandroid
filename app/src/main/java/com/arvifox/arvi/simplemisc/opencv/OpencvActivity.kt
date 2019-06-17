package com.arvifox.arvi.simplemisc.opencv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.app_bar_layout.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.LoaderCallbackInterface
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlin.math.roundToInt

class OpencvActivity : AppCompatActivity(), View.OnTouchListener, CameraBridgeViewBase.CvCameraViewListener2 {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, OpencvActivity::class.java)
        }
    }

    private val TAG = "OCVSample::Activity"

    private var mIsColorSelected = false
    private var mRgba: Mat? = null
    private var mBlobColorRgba: Scalar? = null
    private var mBlobColorHsv: Scalar? = null
    private var mDetector: ColorBlobDetector? = null
    private var mSpectrum: Mat? = null
    private var SPECTRUM_SIZE: Size? = null
    private var CONTOUR_COLOR: Scalar? = null

    private var mOpenCvCameraView: CameraBridgeViewBase? = null

    private val mLoaderCallback = object : BaseLoaderCallback(this) {
        override fun onManagerConnected(status: Int) {
            when (status) {
                LoaderCallbackInterface.SUCCESS -> {
                    mOpenCvCameraView!!.enableView()
                    mOpenCvCameraView!!.setOnTouchListener(this@OpencvActivity)
                }
                else -> {
                    super.onManagerConnected(status)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_opencv)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mOpenCvCameraView = findViewById(R.id.color_blob_detection_activity_surface_view);
        mOpenCvCameraView?.setVisibility(SurfaceView.VISIBLE);
        mOpenCvCameraView?.setCvCameraViewListener(this);
    }

    override fun onPause() {
        mOpenCvCameraView?.disableView();
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    override fun onDestroy() {
        mOpenCvCameraView?.disableView()
        super.onDestroy()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        mRgba = Mat(height, width, CvType.CV_8UC4)
        mDetector = ColorBlobDetector()
        mSpectrum = Mat()
        mBlobColorRgba = Scalar(255.0)
        mBlobColorHsv = Scalar(255.0)
        SPECTRUM_SIZE = Size(200.0, 64.0)
        CONTOUR_COLOR = Scalar(255.0, 0.0, 0.0, 255.0)
    }

    override fun onCameraViewStopped() {
        mRgba?.release()
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        val cols = mRgba?.cols()!!
        val rows = mRgba?.rows()!!

        val xOffset = (mOpenCvCameraView?.getWidth()!! - cols) / 2
        val yOffset = (mOpenCvCameraView?.getHeight()!! - rows) / 2

        val x = event?.getX()?.roundToInt()!! - xOffset
        val y = event?.getY()?.roundToInt()!! - yOffset

        if (x < 0 || y < 0 || x > cols || y > rows) return false

        val touchedRect = Rect()

        touchedRect.x = if (x > 4) x - 4 else 0
        touchedRect.y = if (y > 4) y - 4 else 0

        touchedRect.width = if (x + 4 < cols) x + 4 - touchedRect.x else cols - touchedRect.x
        touchedRect.height = if (y + 4 < rows) y + 4 - touchedRect.y else rows - touchedRect.y

        val touchedRegionRgba = mRgba?.submat(touchedRect)

        val touchedRegionHsv = Mat()
        Imgproc.cvtColor(touchedRegionRgba, touchedRegionHsv, Imgproc.COLOR_RGB2HSV_FULL)

        // Calculate average color of touched region
        mBlobColorHsv = Core.sumElems(touchedRegionHsv)
        val pointCount = touchedRect.width * touchedRect.height
        for (i in 0 until mBlobColorHsv?.`val`?.size!!)
            mBlobColorHsv?.`val`?.set(i, mBlobColorHsv?.`val`?.get(i)!! / pointCount)

        mBlobColorRgba = converScalarHsv2Rgba(mBlobColorHsv!!)

        mDetector?.setHsvColor(mBlobColorHsv)

        Imgproc.resize(mDetector?.getSpectrum(), mSpectrum, SPECTRUM_SIZE, 0.0, 0.0, Imgproc.INTER_LINEAR_EXACT)

        mIsColorSelected = true

        touchedRegionRgba?.release()
        touchedRegionHsv.release()

        return false // don't need subsequent touch events
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        mRgba = inputFrame?.rgba()

        if (mIsColorSelected) {
            mDetector?.process(mRgba)
            val contours = mDetector?.getContours()
            Imgproc.drawContours(mRgba, contours, -1, CONTOUR_COLOR)

            val colorLabel = mRgba?.submat(4, 68, 4, 68)
            colorLabel?.setTo(mBlobColorRgba)

            val spectrumLabel = mRgba?.submat(4, 4 + mSpectrum?.rows()!!, 70, 70 + mSpectrum?.cols()!!)
            mSpectrum?.copyTo(spectrumLabel)
        }

        return mRgba!!
    }

    private fun converScalarHsv2Rgba(hsvColor: Scalar): Scalar {
        val pointMatRgba = Mat()
        val pointMatHsv = Mat(1, 1, CvType.CV_8UC3, hsvColor)
        Imgproc.cvtColor(pointMatHsv, pointMatRgba, Imgproc.COLOR_HSV2RGB_FULL, 4)

        return Scalar(pointMatRgba.get(0, 0))
    }
}
