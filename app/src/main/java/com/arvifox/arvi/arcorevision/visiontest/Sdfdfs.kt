package com.arvifox.arvi.arcorevision.visiontest

import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest
import android.util.Log
import com.google.api.services.vision.v1.model.AnnotateImageRequest
import com.google.api.client.json.gson.GsonFactory
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential
import com.google.api.services.vision.v1.model.Feature
import java.io.IOException


object Sdfdfs {

    fun doInBackground(vararg params: Any): BatchAnnotateImagesResponse? {
        try {
            val credential = GoogleCredential().setAccessToken("accessToken")
            val httpTransport = AndroidHttp.newCompatibleTransport()
            val jsonFactory = GsonFactory.getDefaultInstance()

            val builder = Vision.Builder(httpTransport, jsonFactory, credential)
            val vision = builder.build()

//            val featureList = ArrayList()
//            val labelDetection = Feature()
//            labelDetection.setType("LABEL_DETECTION")
//            labelDetection.setMaxResults(10)
//            featureList.add(labelDetection)
//
//            val textDetection = Feature()
//            textDetection.setType("TEXT_DETECTION")
//            textDetection.setMaxResults(10)
//            featureList.add(textDetection)
//
//            val imageList = ArrayList()
//            val annotateImageRequest = AnnotateImageRequest()
//            val base64EncodedImage = getBase64EncodedJpeg(bitmap)
//            annotateImageRequest.image = base64EncodedImage
//            annotateImageRequest.features = featureList
//            imageList.add(annotateImageRequest)
//
//            val batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
//            batchAnnotateImagesRequest.requests = imageList
//
//            val annotateRequest = vision.images().annotate(batchAnnotateImagesRequest)
//            annotateRequest.disableGZipContent = true
//            Log.d("", "Sending request to Google Cloud")

//            return annotateRequest.execute()

        } catch (e: GoogleJsonResponseException) {
            Log.e("", "Request error: " + e.content)
        } catch (e: IOException) {
            Log.d("", "Request error: " + e.message)
        }

        return null
    }
}