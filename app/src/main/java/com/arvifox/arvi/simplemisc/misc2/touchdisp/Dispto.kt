package com.arvifox.arvi.simplemisc.misc2.touchdisp

import android.view.MotionEvent
import android.view.ScaleGestureDetector
import androidx.appcompat.app.AppCompatActivity

/*
https://stfalcon.com/ru/blog/post/learning-android-gestures
 */

class Diact : AppCompatActivity() {

    val sc = sd()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        sc.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    fun sd(): ScaleGestureDetector {
        return ScaleGestureDetector(this, object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onScaleEnd(p0: ScaleGestureDetector?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onScale(p0: ScaleGestureDetector?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }
}