package com.arvifox.arvi.google.ar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Toast
import com.arvifox.arvi.R
import com.arvifox.arvi.google.utils.ArUtils
import com.arvifox.arvi.utils.Logger
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import kotlinx.android.synthetic.main.app_bar_layout.*


class HelloSceneActivity : AppCompatActivity() {

    private var arFragment: ArFragment? = null
    private var model: ModelRenderable? = null
    private var m: Material? = null

    private var flag: Boolean = false

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, HelloSceneActivity::class.java)
        }
    }

    // CompletableFuture requires api level 24
    // FutureReturnValueIgnored is not valid
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!ArUtils.checkIsSupportedDeviceOrFinish(this)) {
            return
        }
        setContentView(R.layout.activity_hello_scene)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        arFragment = supportFragmentManager.findFragmentById(R.id.arFragment) as ArFragment

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

        MaterialFactory.makeOpaqueWithColor(this, Color(android.graphics.Color.BLUE)).thenAccept { mat -> m = mat }

        arFragment?.setOnTapArPlaneListener { hitResult: HitResult, plane: Plane, motionEvent: MotionEvent ->
            Logger.d { "tap plane" }
            if (!flag) {
                if (model == null) {
                    return@setOnTapArPlaneListener
                }

                // Create the Anchor.
                val anchor = hitResult.createAnchor()
                val anchorNode = AnchorNode(anchor)
                anchorNode.setParent(arFragment?.arSceneView?.scene)

                // create a cube
//                val v = Vector3(hitResult.hitPose.tx(), hitResult.hitPose.ty(), hitResult.hitPose.tz())
                val v = Vector3(0f, 0f, -1f)
                model = ShapeFactory.makeCube(Vector3(0.2f, 0.2f, 0.2f), v, m)

                // Create the transformable andy and add it to the anchor.
                val andy = TransformableNode(arFragment?.transformationSystem)
                andy.setParent(anchorNode)
//                model?.material?.setFloat3("baseColor", Color(android.graphics.Color.GREEN))
                andy.renderable = model
                andy.select()
                flag = true
            } else {
                Logger.d { "material size =" + model?.submeshCount }
                model?.material?.setFloat3("baseColor", Color(android.graphics.Color.BLUE))
//                val future = Texture.builder().setSource(this, R.drawable.soccer_bal).build()
//                future?.thenAccept { f -> model?.getMaterial(0)?.setTexture("baseColorMap", f) }
//                        ?.exceptionally {
//                            Logger.d { "error" }
//                            null
//                        }
            }
        }
    }
}
