package com.arvifox.arvi.domain.interview

import androidx.appcompat.app.AppCompatActivity
import java.lang.ref.WeakReference

class ActivityLeakTask : AppCompatActivity() {

    inner class LeakJob(link: WeakReference<ActivityLeakTask>) {
        fun start() {
            Thread {
                // some
            }.start()
        }
    }

    fun runJob() {
        val wr = WeakReference(this)
        val lj = LeakJob(wr)
        lj.start()

    }
}

/*
class ActivityLeakTask : AppCompatActivity() {

    class LeakJob private constructor(private val weakActivity: WeakReference<ActivityLeakTask>) {
        fun start() {
            Thread {
                // some work
                val activity = weakActivity.get()
                if (activity != null && !activity.isFinishing) {
                    // Выполняем работу, если активность все еще жива
                }
            }.start()
        }

        companion object {
            fun create(activity: ActivityLeakTask): LeakJob {
                return LeakJob(WeakReference(activity))
            }
        }
    }

    fun runJob() {
        val lj = LeakJob.create(this)
        lj.start()
    }
}
*/