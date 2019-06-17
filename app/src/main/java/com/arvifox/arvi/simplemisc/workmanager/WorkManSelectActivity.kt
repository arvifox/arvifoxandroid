package com.arvifox.arvi.simplemisc.workmanager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.text.Spanned
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.Logger
import kotlinx.android.synthetic.main.activity_work_man_select.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class WorkManSelectActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, WorkManSelectActivity::class.java)
        }
    }

    private val REQUEST_CODE_IMAGE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_work_man_select)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btnSelect.setOnClickListener {
            val chooseIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(chooseIntent, REQUEST_CODE_IMAGE)
        }

        selectStockImage.setOnClickListener {
            val filterIntent = Intent(this, WorkManagerActivity::class.java)
            filterIntent.putExtra("KEY_IMAGE_URI", StockImages.randomStockImage())
            startActivity(filterIntent)
        }
    }

    /** Image Selection  */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_IMAGE -> data?.let { handleImageRequestResult(data) }
                else -> Logger.d { "Unknown request code." }
            }
        } else {
            Logger.e { String.format("Unexpected Result code %s", resultCode) }
        }
    }

    private fun handleImageRequestResult(intent: Intent) {
        // If clipdata is available, we use it, otherwise we use data
        val imageUri: Uri? = intent.clipData?.let {
            it.getItemAt(0).uri
        } ?: intent.data

        if (imageUri == null) {
            Logger.e { "Invalid input image Uri." }
            return
        }

        val filterIntent = Intent(this, WorkManagerActivity::class.java)
        filterIntent.putExtra("KEY_IMAGE_URI", imageUri.toString())
        startActivity(filterIntent)
    }

    private fun fromHtml(input: String): Spanned {
        return if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            Html.fromHtml(input, Html.FROM_HTML_MODE_COMPACT)
        } else {
            // method deprecated at API 24.
            Html.fromHtml(input)
        }
    }
}
