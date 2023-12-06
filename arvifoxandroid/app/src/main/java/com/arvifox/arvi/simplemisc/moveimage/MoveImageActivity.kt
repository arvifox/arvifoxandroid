package com.arvifox.arvi.simplemisc.moveimage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityMoveImageBinding

class MoveImageActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, MoveImageActivity::class.java)
        }
    }

    private var xDelta: Int = 0
    private var yDelta: Int = 0

    private lateinit var binding: ActivityMoveImageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoveImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnImageAdd.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            startActivityForResult(intent, 1234)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1234) {
                data?.data?.let { createImage(it) }
            }
        }
    }

    private fun createImage(uri: Uri) {
//        doesn't work
//        val file = File(uri.path)
//        val b = BitmapFactory.decodeFile(file.absolutePath)
        val b = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
        val iv = ImageView(this)
        val prm = FrameLayout.LayoutParams(300, 300)
        iv.layoutParams = prm
        iv.setImageBitmap(b)
        iv.setOnTouchListener(onTouchListener)
        binding.framelayoutimagemove.addView(iv)
    }

    private val onTouchListener = View.OnTouchListener { v, event ->
        if (v != null && event != null) {
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val lParams = v.layoutParams as FrameLayout.LayoutParams

                    xDelta = x - lParams.leftMargin
                    yDelta = y - lParams.topMargin
                }
                MotionEvent.ACTION_UP -> {
                    Toast.makeText(applicationContext, "Объект перемещён", Toast.LENGTH_SHORT)
                        .show()
                }
                MotionEvent.ACTION_MOVE -> {
                    if (x - xDelta + v.width <= binding.framelayoutimagemove.width
                        && y - yDelta + v.height <= binding.framelayoutimagemove.height
                        && x - xDelta >= 0
                        && y - yDelta >= 0
                    ) {
                        val layoutParams = v.layoutParams as FrameLayout.LayoutParams
                        layoutParams.leftMargin = x - xDelta
                        layoutParams.topMargin = y - yDelta
                        layoutParams.rightMargin = 0
                        layoutParams.bottomMargin = 0
                        v.layoutParams = layoutParams
                    }
                }
            }
            binding.framelayoutimagemove.invalidate()
            true
        } else false
    }
}
