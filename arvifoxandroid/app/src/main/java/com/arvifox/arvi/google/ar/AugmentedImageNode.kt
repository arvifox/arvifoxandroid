package com.arvifox.arvi.google.ar

import android.content.Context
import android.net.Uri
import android.os.Build
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.Logger

import com.google.ar.core.AugmentedImage
import com.google.ar.core.Pose
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.rendering.ShapeFactory

import java.util.concurrent.CompletableFuture

class AugmentedImageNode(context: Context, filename: String) : AnchorNode() {

    private val TAG = "AugmentedImageNode"
    private var modelFuture: CompletableFuture<ModelRenderable>? = null

    /**
     * Called when the AugmentedImage is detected and should be rendered. A Sceneform node tree is
     * created based on an Anchor created from the image.
     *
     * @param image captured by your camera
     */
    var image: AugmentedImage? = null

    init {
        // Upon construction, start loading the modelFuture
        if (modelFuture == null && Build.VERSION.SDK_INT >= 24) {
            modelFuture = ModelRenderable.builder()
                    .setSource(context, R.raw.model)
                    .build()
        }
    }

    @SuppressWarnings("AndroidApiChecker", "FutureReturnValueIgnored")
    fun setImageI(im: AugmentedImage) {
        if (Build.VERSION.SDK_INT < 24) return
        image = im
        if (!modelFuture!!.isDone) {
            CompletableFuture.allOf(modelFuture!!).thenAccept { setImageI(im) }
                    .exceptionally { throwable ->
                        Logger.e { "Exception loading" }
                        null
                    }
        }
        anchor = image?.createAnchor(im.centerPose)
        val node = Node()
        val pose = Pose.makeTranslation(0.0f, 0.0f, 0.25f)
        node.setParent(this)
        node.localPosition = Vector3(pose.tx(), pose.ty(), pose.tz())
        node.localRotation = Quaternion(pose.qx(), pose.qy(), pose.qz(), pose.qw())
        node.renderable = modelFuture!!.getNow(null)
    }
}