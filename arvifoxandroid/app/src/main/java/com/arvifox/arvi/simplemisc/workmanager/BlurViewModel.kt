package com.arvifox.arvi.simplemisc.workmanager

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.work.*

class BlurViewModel : ViewModel() {

    internal var imageUri: Uri? = null
    internal var outputUri: Uri? = null
    internal val outputWorkInfoItems: LiveData<List<WorkInfo>>
    private val workManager: WorkManager = WorkManager.getInstance()
    private val imwn = "IMAGE_MANIPULATION_WORK_NAME"

    init {
        // This transformation makes sure that whenever the current work Id changes the WorkStatus
        // the UI is listening to changes
        outputWorkInfoItems = workManager.getWorkInfosByTagLiveData("TAG_OUTPUT")
    }

    internal val outputStatus: LiveData<List<WorkInfo>>
        get() = workManager.getWorkInfosByTagLiveData("TAG_OUTPUT")

    internal fun apply(imageOperations: ImageOperations) {
        imageOperations.continuation.enqueue()
    }

    internal fun applyBlur(blurLevel: Int) {
//        workManager.enqueue(OneTimeWorkRequest.from(BlurWorker::class.java))

        // Add WorkRequest to Cleanup temporary images
        var continuation = workManager
                .beginUniqueWork(
                        imwn, ExistingWorkPolicy.REPLACE,
                        OneTimeWorkRequest.from(CleanupWorker::class.java)
                )

        // Add WorkRequests to blur the image the number of times requested
        for (i in 0 until blurLevel) {
            val blurBuilder = OneTimeWorkRequestBuilder<BlurWorker>()

            // Input the Uri if this is the first blur operation
            // After the first blur operation the input will be the output of previous
            // blur operations.
            if (i == 0) {
                blurBuilder.setInputData(createInputDataForUri())
            }

            continuation = continuation.then(blurBuilder.build())
        }

        // Create charging constraint
        val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .build()

        // Add WorkRequest to save the image to the filesystem
        val save = OneTimeWorkRequestBuilder<SaveImageToFileWorker>()
                .setConstraints(constraints)
                .addTag("TAG_OUTPUT")
                .build()
        continuation = continuation.then(save)

        // Actually start the work
        continuation.enqueue()
    }

    /**
     * Cancel work using the work's unique name
     */
    internal fun cancelWork() {
        workManager.cancelUniqueWork(imwn)
    }

    /**
     * Creates the input data bundle which includes the Uri to operate on
     * @return Data which contains the Image Uri as a String
     */
    private fun createInputDataForUri(): Data {
        val builder = Data.Builder()
        imageUri?.let {
            builder.putString("KEY_IMAGE_URI", imageUri.toString())
        }
        return builder.build()
    }

    private fun uriOrNull(uriString: String?): Uri? {
        return if (!uriString.isNullOrEmpty()) {
            Uri.parse(uriString)
        } else {
            null
        }
    }

    /**
     * Setters
     */
    internal fun setImageUri(uri: String?) {
        imageUri = uriOrNull(uri)
    }

    internal fun setOutputUri(outputImageUri: String?) {
        outputUri = uriOrNull(outputImageUri)
    }
}