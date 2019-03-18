package com.arvifox.arvi.simplemisc.workmanager

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_manager)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
    }
}
