package com.arvifox.arvi.simplemisc.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.arvifox.arvi.R
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

        btnStart2.setOnClickListener { viewModel.applyBlur(2) }
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
//                showWorkFinished()
                // Normally this processing, which is not directly related to drawing views on
                // screen would be in the ViewModel. For simplicity we are keeping it here.
//                val outputImageUri = workInfo.outputData.getString(KEY_IMAGE_URI)
                // If there is an output file show "See File" button
//                if (!outputImageUri.isNullOrEmpty()) {
//                    viewModel.setOutputUri(outputImageUri as String)
//                    outputButton.visibility = View.VISIBLE
//                }
            } else {
//                showWorkInProgress()
            }
        }
    }
}
