package com.arvifox.arvi.simplemisc.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Checkable
import android.widget.ProgressBar
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.arvifox.arvi.R
import com.arvifox.arvi.databinding.ActivityWorkManagerBinding
import com.bumptech.glide.Glide
import java.util.concurrent.TimeUnit

class WorkManagerActivity : AppCompatActivity() {

    companion object {

        internal fun newIntent(context: Context, imageUri: Uri): Intent {
            val intent = Intent(context, WorkManagerActivity::class.java)
            intent.putExtra("KEY_IMAGE_URI", imageUri.toString())
            return intent
        }
    }

    private lateinit var viewModel: BlurViewModel

    private var mImageUri: Uri? = null
    private var mOutputImageUri: Uri? = null
    private lateinit var binding: ActivityWorkManagerBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get the ViewModel
        viewModel = ViewModelProviders.of(this).get(BlurViewModel::class.java)
        // Show work status
//        viewModel.outputWorkInfoItems.observe(this, workInfosObserver())

        // Check to see if we have output.
        viewModel.outputStatus.observe(this, Observer { listOfInfos ->
            if (listOfInfos == null || listOfInfos.isEmpty()) {
                return@Observer
            }

            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val info = listOfInfos[0]
            val finished = info.state.isFinished
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            val go = findViewById<Button>(R.id.go)
            val cancel = findViewById<Button>(R.id.cancel)
            val output = findViewById<Button>(R.id.output)
            if (!finished) {
                progressBar.visibility = View.VISIBLE
                cancel.visibility = View.VISIBLE
                go.visibility = View.GONE
                output.visibility = View.GONE
            } else {
                progressBar.visibility = View.GONE
                cancel.visibility = View.GONE
                go.visibility = View.VISIBLE

                val outputData = info.outputData
                val outputImageUri = outputData.getString("KEY_IMAGE_URI")

                if (!TextUtils.isEmpty(outputImageUri)) {
                    mOutputImageUri = Uri.parse(outputImageUri)
                    output.visibility = View.VISIBLE
                }
            }
        })

        // Image uri should be stored in the ViewModel; put it there then display
        val imageUriExtra = intent.getStringExtra("KEY_IMAGE_URI")
        mImageUri = Uri.parse(imageUriExtra)
        viewModel.setImageUri(imageUriExtra)
        viewModel.imageUri?.let { imageUri ->
            Glide.with(this).load(imageUri).into(binding.imageView)
        }

        binding.btnStart1.setOnClickListener {
            // optionally, add constraints like power, network availability
            val constraints: Constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiresDeviceIdle(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val myOneTimeWorkRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setInitialDelay(20, TimeUnit.MINUTES)
//                    .setInputData()
                .addTag("tratata")
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    10000, TimeUnit.MILLISECONDS
                )
                .setConstraints(constraints).build()
            WorkManager.getInstance().enqueue(myOneTimeWorkRequest)
        }

        binding.go.setOnClickListener {
            val applyWaterColor = isChecked(R.id.filter_watercolor)
            val applyGrayScale = isChecked(R.id.filter_grayscale)
            val applyBlur = isChecked(R.id.filter_blur)
            val save = isChecked(R.id.save)
            val upload = isChecked(R.id.upload)

            val imageOperations = ImageOperations.Builder(mImageUri!!)
                .setApplyWaterColor(applyWaterColor)
                .setApplyGrayScale(applyGrayScale)
                .setApplyBlur(applyBlur)
                .setApplySave(save)
                .setApplyUpload(upload)
                .build()

            viewModel.apply(imageOperations)
//            viewModel.applyBlur(blurLevel)
        }

        // Setup view output image file button
        binding.output.setOnClickListener {
            if (mOutputImageUri != null) {
                val actionView = Intent(Intent.ACTION_VIEW, mOutputImageUri)
                if (actionView.resolveActivity(packageManager) != null) {
                    startActivity(actionView)
                }
            }
//            viewModel.outputUri?.let { currentUri ->
//                val actionView = Intent(Intent.ACTION_VIEW, currentUri)
//                actionView.resolveActivity(packageManager)?.run {
//                    startActivity(actionView)
//                }
//            }
        }

        // Hookup the Cancel button
        binding.cancel.setOnClickListener { viewModel.cancelWork() }
    }

    private fun workInfosObserver(): Observer<List<WorkInfo>> {
        return Observer { listOfWorkInfo ->
            // Note that these next few lines grab a single WorkInfo if it exists
            // This code could be in a Transformation in the ViewModel; they are included here
            // so that the entire process of displaying a WorkInfo is in one location.
            // If there are no matching work info, do nothing
            if (listOfWorkInfo.isNullOrEmpty()) {
                return@Observer
            }
            // We only care about the one output status.
            // Every continuation has only one worker tagged TAG_OUTPUT
            val workInfo = listOfWorkInfo[0]
            if (workInfo.state.isFinished) {
                showWorkFinished()
                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.
                val outputImageUri = workInfo.outputData.getString("KEY_IMAGE_URI")
                // If there is an output file show "See File" button
                if (!outputImageUri.isNullOrEmpty()) {
                    viewModel.setOutputUri(outputImageUri as String)
//                    see_file_button.visibility = View.VISIBLE
                }
            } else {
                showWorkInProgress()
            }
        }
    }

    private fun isChecked(@IdRes resourceId: Int): Boolean {
        val view = findViewById<View>(resourceId)
        return view is Checkable && (view as Checkable).isChecked
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private fun showWorkInProgress() {
//        progress_bar.visibility = View.VISIBLE
//        cancel_button.visibility = View.VISIBLE
//        go_button.visibility = View.GONE
//        see_file_button.visibility = View.GONE
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private fun showWorkFinished() {
//        progress_bar.visibility = View.GONE
//        cancel_button.visibility = View.GONE
//        go_button.visibility = View.VISIBLE
    }

//    private val blurLevel: Int
//        get() =
//            when (radio_blur_group.checkedRadioButtonId) {
//                R.id.radio_blur_lv_1 -> 1
//                R.id.radio_blur_lv_2 -> 2
//                R.id.radio_blur_lv_3 -> 3
//                else -> 1
//            }
}
