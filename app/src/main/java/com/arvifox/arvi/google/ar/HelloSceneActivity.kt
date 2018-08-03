package com.arvifox.arvi.google.ar

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Toast
import com.arvifox.arvi.R
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.app_bar_layout.*


class HelloSceneActivity : AppCompatActivity() {

    private var arFragment: ArFragment? = null
    private var model: ModelRenderable? = null

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, HelloSceneActivity::class.java)
        }
    }

    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    @SuppressWarnings("AndroidApiChecker", "FutureReturnValueIgnored")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!ArUtils.checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        setContentView(R.layout.activity_hello_scene)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

        if (Build.VERSION.SDK_INT < 24) return

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, R.raw.model)
                .build()
                .thenAccept { renderable -> model = renderable }
                .exceptionally()
                { _ ->
                    val toast = Toast.makeText(this, "Unable to load renderable", Toast.LENGTH_LONG)
                    toast.setGravity(Gravity.CENTER, 0, 0)
                    toast.show()
                    null
                }

        arFragment?.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            if (model == null) {
                return@setOnTapArPlaneListener
            }

            // Create the Anchor.
            val anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor)
            anchorNode.setParent(arFragment?.arSceneView?.scene)

            // Create the transformable andy and add it to the anchor.
            val andy = TransformableNode(arFragment?.transformationSystem)
            andy.setParent(anchorNode)
            andy.renderable = model
            andy.select()
        }
    }
}
