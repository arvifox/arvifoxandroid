package com.arvifox.arvi.utils

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.Qwe.afterMeasured
import kotlin.math.abs

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
//        clMain.viewTreeObserver.addOnGlobalLayoutListener(object :
//            ViewTreeObserver.OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                clMain.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                val v = window.decorView.findViewById<View>(android.R.id.statusBarBackground)
//                Log.d("foxx", "hhh=${v.height}")
//                clMain.setPadding(0, v.height, 0, 0)
//            }
//        })
//        clMain.waitForLayout {
//            val v = window.decorView.findViewById<View>(android.R.id.statusBarBackground)
//            Log.d("foxx", "hhh=${v.height}")
//            clMain.setPadding(0, v.height, 0, 0)
//        }

//        clMain.afterMeasured {
//            val v = window.decorView.findViewById<View>(android.R.id.statusBarBackground)
//            Log.d("foxx", "hhh=${v.height}")
//            clMain.setPadding(0, v.height, 0, 0)
//        }

        window.decorView.afterMeasured {
            val v = findViewById<View>(android.R.id.statusBarBackground)
            Log.d("foxx", "hhh=${v.height}")
            //clMain.setPadding(0, v.height, 0, 0)
            eee()
            getStatusBarHeight()
        }

        ffdf()
    }

    private fun eee() {
        val rectangle = Rect()
        val window = window
        // not good way because of multiwindow mode
        window.decorView.getWindowVisibleDisplayFrame(rectangle)
        val statusBarHeight = rectangle.top
        val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).top
        val titleBarHeight = abs(contentViewTop - statusBarHeight)
        Log.d("foxx", "www= $titleBarHeight")
    }

    private fun ffdf() {
//        ViewCompat.setOnApplyWindowInsetsListener(tvMain) { v, i ->
//            Log.d("foxx", "zxzxz=${i.systemWindowInsetTop}")
//            i
//        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        Log.d("foxx", "xxx=$result")
        return result
    }
}

object Qwe {

    inline fun View.waitForLayout(crossinline f: () -> Unit) = with(viewTreeObserver) {
        addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                removeOnGlobalLayoutListener(this)
                f()
            }
        })
    }

    inline fun <T : View> T.afterMeasured(crossinline f: T.() -> Unit) {
        viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (measuredWidth > 0 && measuredHeight > 0) {
                    viewTreeObserver.removeOnGlobalLayoutListener(this)
                    f()
                }
            }
        })
    }
}