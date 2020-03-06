package com.arvifox.arvi.google.googleapi.visiontest

import android.accounts.AccountManager
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.FormatUtils.takeByteArray
import com.arvifox.arvi.utils.Logger
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.model.*
import com.squareup.picasso.Picasso
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_vision_api_test.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class VisionApiTestActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, VisionApiTestActivity::class.java)
        }
    }

    private val am: AccountManager by lazy { AccountManager.get(this) }
    private lateinit var mainToken: String
    private lateinit var iis: ByteArray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vision_api_test)

        val tb = toolbar
        setSupportActionBar(tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        tvVisionTest.movementMethod = ScrollingMovementMethod()

        btnChoose.setOnClickListener { btnclick() }

        gettingToken()
    }

    fun hasToken(token: String) {
        etToken.setText(token, TextView.BufferType.EDITABLE)
        mainToken = token
    }

    private fun btnclick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 845)
    }

    private fun makeRequest() {
        val sing: Single<BatchAnnotateImagesResponse> = Single.fromCallable {
            val credential = GoogleCredential().setAccessToken(mainToken)
            val httpTransport = AndroidHttp.newCompatibleTransport()
            val jsonFactory = GsonFactory.getDefaultInstance()

            val builder = Vision.Builder(httpTransport, jsonFactory, credential)
            val vision = builder.build()

            val featureList = ArrayList<Feature>()
            val labelDetection = Feature()
            labelDetection.type = "LABEL_DETECTION"
            labelDetection.maxResults = 10
            featureList.add(labelDetection)

            val textDetection = Feature()
            textDetection.type = "TEXT_DETECTION"
            textDetection.maxResults = 10
            featureList.add(textDetection)

            val imageList = ArrayList<AnnotateImageRequest>()
            val annotateImageRequest = AnnotateImageRequest()
            val im = Image().encodeContent(iis)
            annotateImageRequest.image = im
            annotateImageRequest.features = featureList
            imageList.add(annotateImageRequest)

            val batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
            batchAnnotateImagesRequest.requests = imageList

            val annotateRequest = vision.images().annotate(batchAnnotateImagesRequest)
            annotateRequest.disableGZipContent = true
            Logger.d { "Sending request to Google Cloud" }

            return@fromCallable annotateRequest.execute()
        }
        sing.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe { t1, t2 ->
                if (t2 != null) {
                    tvVisionTest.text = t2.message
                } else {
                    val sb = StringBuilder()
                    sb.append("rs=" + t1.responses.size)
                    val ir = t1.responses[0]
                    sb.append("labels=" + ir.labelAnnotations.size)
                    for (ea in ir.labelAnnotations) {
                        sb.append("\n" + ea.toPrettyString())
                    }
                    tvVisionTest.text = sb.toString()
                }
            }
    }

    private fun gettingToken() {
        val getOAuthToken = GetOAuthToken(
            this, am.getAccountsByType("com.google")[0],
            "oauth2:https://www.googleapis.com/auth/cloud-vision", 246
        )
        getOAuthToken.execute(null as Void?)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 246 && resultCode == Activity.RESULT_OK) {
            gettingToken()
        }
        if (requestCode == 845 && resultCode == Activity.RESULT_OK) {
            Picasso.Builder(this).build().load(data!!.data)
                .placeholder(R.drawable.ic_hibin)
                .error(R.drawable.ic_hibin)
                .into(ivVisionTest)

            val d = this.contentResolver.openInputStream(data.data!!)
            iis = d?.takeByteArray()!!
//            ivVisionTest.setImageBitmap(BitmapFactory.decodeStream(d))
//            MediaStore.Images.Media.getBitmap()
            d.close()
            makeRequest()
        }
    }
}
