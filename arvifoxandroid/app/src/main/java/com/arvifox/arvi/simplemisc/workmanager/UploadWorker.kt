package com.arvifox.arvi.simplemisc.workmanager

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.work.*
import com.arvifox.arvi.simplemisc.workmanager.imgur.ImgurApi

/**
 * Uploads an image to Imgur using the [ImgurApi].
 */
class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    companion object {
        private const val TAG = "UploadWorker"
    }

    override fun doWork(): Result {
        var imageUriInput: String? = null
        try {
            val args = inputData
            imageUriInput = args.getString("KEY_IMAGE_URI")
            val imageUri = Uri.parse(imageUriInput)
            val imgurApi = ImgurApi.instance.value
            // Upload the image to Imgur.
            val response = imgurApi.uploadImage(imageUri).execute()
            // Check to see if the upload succeeded.
            if (!response.isSuccessful) {
                val errorBody = response.errorBody()
                val error = errorBody?.string()
                val message = String.format("Request failed %s (%s)", imageUriInput, error)
                Log.e(TAG, message)
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
                return Result.failure()
            } else {
                val imageResponse = response.body()
                var outputData = workDataOf()
                if (imageResponse != null) {
                    val imgurLink = imageResponse.data!!.link
                    // Set the result of the worker by calling setOutputData().
                    outputData = Data.Builder()
                            .putString("KEY_IMAGE_URI", imgurLink)
                            .build()
                }
                return Result.success(outputData)
            }
        } catch (e: Exception) {
            val message = String.format("Failed to upload image with URI %s", imageUriInput)
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            Log.e(TAG, message)
            return Result.failure()
        }

    }
}
