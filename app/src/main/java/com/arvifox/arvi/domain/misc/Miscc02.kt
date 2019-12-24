package com.arvifox.arvi.domain.misc

import android.annotation.SuppressLint
import android.content.Context
import android.text.SpannableString
import android.text.style.LineBackgroundSpan
import android.text.style.LineHeightSpan
import androidx.core.text.PrecomputedTextCompat
import com.arvifox.arvi.R
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.ShapeAppearanceModel
import com.google.android.material.textview.MaterialTextView

object Miscc02 {
    fun dfdf() {
        val w = Widd()
        val li = { wi: Widd -> println("wii = $wi") }
        w.addLis(li)
        println("count=${w.count}")
        w.removeLis(li)
        println("count=${w.count}")
    }

    fun dfdf22() {
        val w = Widd()
        val li = Widd.Listener { wi: Widd -> println("wii = $wi") }
        w.addLis(li)
        println("count=${w.count}")
        w.removeLis(li)
        println("count=${w.count}")
    }
}

object slfdkj {
    fun sdf(cc: Context) {
        val sss = MaterialShapeDrawable(
            ShapeAppearanceModel.builder(
                cc,
                R.style.ShapeAppp,
                0
            ).build()
        )
    }

    fun dfdf(tt: MaterialTextView) {
        tt.setTextFuture(
            PrecomputedTextCompat.getTextFuture(
                "", tt.textMetricsParamsCompat, null
            )
        )
    }

    @SuppressLint("NewApi")
    fun fjkj(tt: MaterialTextView) {
        val l = LineHeightSpan.Standard(20)
        val ll = LineBackgroundSpan.Standard(29347)
        val ss = SpannableString("skjdfkdj").apply {
            setSpan(l, 0, 2, 0)
            setSpan(ll, 0, 2, 0)
        }
        tt.text = ss
    }
}