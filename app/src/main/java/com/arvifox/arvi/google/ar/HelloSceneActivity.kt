package com.arvifox.arvi.google.ar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Gravity
import android.view.MotionEvent
import android.widget.Toast
import com.arvifox.arvi.R
import com.arvifox.arvi.google.utils.ArUtils
import com.arvifox.arvi.utils.Logger
import com.google.ar.core.HitResult
import com.google.ar.core.Plane
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.*
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.google.ar.sceneform.rendering.ShapeFactory
import com.google.ar.sceneform.rendering.MaterialFactory
import com.google.ar.sceneform.math.Quaternion
import kotlinx.android.synthetic.main.activity_hello_scene.*
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

        btnDrawPlane.setOnClickListener { drawPlane() }
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
                Logger.d { "anchor=" + anchorNode.worldPosition }

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

    @SuppressLint("NewApi")
    private fun drawPlane() {
        val allTrackables = arFragment?.arSceneView?.session?.getAllTrackables(Plane::class.java)
        val tr = allTrackables?.elementAt(0)
        Logger.d { "s=" + allTrackables?.size + " t=" + tr?.type }
        Logger.d { "cp=" + tr?.centerPose + " ex=" + tr?.extentX + " ez=" + tr?.extentZ }
        Logger.d { "fbs=" + tr?.polygon?.array()?.size }
        val anc = tr?.createAnchor(tr.centerPose)
        val ann = AnchorNode(anc)
        ann.setParent(arFragment?.arSceneView?.scene)
        val v = Vector3(0f, 0f, 0f)
        model = ShapeFactory.makeCube(Vector3(0.2f, 0.2f, 0.2f), v, m)
        val andy = TransformableNode(arFragment?.transformationSystem)
        andy.setParent(ann)
        andy.renderable = model
//        andy.select()
        val sz = tr?.polygon?.array()?.size!!
        val fp1 = tr.centerPose.transformPoint(floatArrayOf(tr?.polygon?.array()?.get(0)!!, 0f, tr?.polygon?.array()?.get(1)!!))
        val fp2 = tr.centerPose.transformPoint(floatArrayOf(tr?.polygon?.array()?.get(sz / 2)!!, 0f, tr?.polygon?.array()?.get(sz / 2 + 1)!!))
        drawLine(Vector3(fp1[0], fp1[1], fp1[2]), Vector3(fp2[0], fp2[1], fp2[2]), ann)
    }

    @SuppressLint("NewApi")
    private fun drawLine(v1: Vector3, v2: Vector3, anchorNode: AnchorNode) {
        Logger.d { "v1=" + v1 + " v2=" + v2 }
        val lineNode = Node()
        val difference = Vector3.subtract(v1, v2)
        val directionFromTopToBottom = difference.normalized()
        val rotationFromAToB = Quaternion.lookRotation(directionFromTopToBottom, Vector3.up())
        MaterialFactory.makeOpaqueWithColor(this, Color(android.graphics.Color.GREEN))
                .thenAccept { material ->
                    val lineRenderable = ShapeFactory.makeCube(Vector3(.01f, .01f, difference.length()),
                            Vector3.zero(), material)
                    lineNode.setParent(arFragment?.arSceneView?.scene)
                    lineNode.renderable = lineRenderable
//                    lineNode.localPosition = Vector3.add(v1, v2).scaled(.5f)
                    lineNode.localPosition = v1
                    lineNode.localRotation = rotationFromAToB
                }
    }
}
