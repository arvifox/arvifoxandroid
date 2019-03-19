package com.arvifox.arvi.simplemisc.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.arvifox.arvi.R
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_work_manager.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import java.util.concurrent.TimeUnit

class WorkManagerActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, WorkManagerActivity::class.java)
        }
    }

    private lateinit var viewModel: BlurViewModel

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_manager)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Get the ViewModel
        viewModel = ViewModelProviders.of(this).get(BlurViewModel::class.java)
        // Show work status
        viewModel.outputWorkInfoItems.observe(this, workInfosObserver())

        // Image uri should be stored in the ViewModel; put it there then display
        val imageUriExtra = intent.getStringExtra("KEY_IMAGE_URI")
        viewModel.setImageUri(imageUriExtra)
        viewModel.imageUri?.let { imageUri ->
            Glide.with(this).load(imageUri).into(image_view)
        }

        btnStart1.setOnClickListener {
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
                    .setBackoffCriteria(BackoffPolicy.LINEAR,
                            OneTimeWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                    .setConstraints(constraints).build()
            WorkManager.getInstance().enqueue(myOneTimeWorkRequest)
        }

        go_button.setOnClickListener { viewModel.applyBlur(blurLevel) }

        // Setup view output image file button
        see_file_button.setOnClickListener {
            viewModel.outputUri?.let { currentUri ->
                val actionView = Intent(Intent.ACTION_VIEW, currentUri)
                actionView.resolveActivity(packageManager)?.run {
                    startActivity(actionView)
                }
            }
        }

        // Hookup the Cancel button
        cancel_button.setOnClickListener { viewModel.cancelWork() }
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
                    see_file_button.visibility = View.VISIBLE
                }
            } else {
                showWorkInProgress()
            }
        }
    }

    /**
     * Shows and hides views for when the Activity is processing an image
     */
    private fun showWorkInProgress() {
        progress_bar.visibility = View.VISIBLE
        cancel_button.visibility = View.VISIBLE
        go_button.visibility = View.GONE
        see_file_button.visibility = View.GONE
    }

    /**
     * Shows and hides views for when the Activity is done processing an image
     */
    private fun showWorkFinished() {
        progress_bar.visibility = View.GONE
        cancel_button.visibility = View.GONE
        go_button.visibility = View.VISIBLE
    }

    private val blurLevel: Int
        get() =
            when (radio_blur_group.checkedRadioButtonId) {
                R.id.radio_blur_lv_1 -> 1
                R.id.radio_blur_lv_2 -> 2
                R.id.radio_blur_lv_3 -> 3
                else -> 1
            }
}
