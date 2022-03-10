package com.arvifox.shadowtest

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils
import com.google.android.material.shape.ShapeAppearanceModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.Version. > )

        val sa = ShapeAppearanceModel.builder()
            .setAllCorners(CornerFamily.ROUNDED, 40f)
            .build()
        val s = MaterialShapeDrawable(sa).apply {
            fillColor = ContextCompat.getColorStateList(this@MainActivity, R.color.col1)
            //setTint(ContextCompat.getColor(this@MainActivity, R.color.col1))
            //paintStyle = Paint.Style.FILL
            setShadowColor(android.R.color.holo_green_dark)
            shadowCompatibilityMode = MaterialShapeDrawable.SHADOW_COMPAT_MODE_DEFAULT
            elevation = 23f
            translationZ = 10f
        }
        findViewById<ImageButton>(R.id.btn1).background = s

        findViewById<IconView>(R.id.ico1).icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_round, null)

        findViewById<IcoonView>(R.id.ico2).apply {
            icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_round, null)
            offset = -8f
        }

        findViewById<IcoonView>(R.id.ico3).apply {
            icon = ResourcesCompat.getDrawable(resources, R.drawable.ic_asd, null)
            offset = 8f
        }
    }
}