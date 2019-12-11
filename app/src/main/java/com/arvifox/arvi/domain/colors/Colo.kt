package com.arvifox.arvi.domain.colors

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import com.arvifox.arvi.R

// https://jorgecastillo.dev/contrasting-text-and-icons-over-background

object Soco {

    fun @receiver:ColorInt Int.isDark(): Boolean =
        androidx.core.graphics.ColorUtils.calculateLuminance(this) < 0.5

    fun sdf(colorHex: TextView, favIcon: ImageView, c: Context) {
        val color = Color.parseColor("#e91e63")

        if (color.isDark()) {
            colorHex.setTextColor(ContextCompat.getColor(c, R.color.white))
            favIcon.setImageResource(R.drawable.ic_hibin)
        } else {
            colorHex.setTextColor(ContextCompat.getColor(c, R.color.grey_20))
            favIcon.setImageResource(R.drawable.ic_hibin)
        }
    }

    private fun bindFavIcon(bitmap: Bitmap, favIcon: ImageView) {
        val iconSize = favIcon.width

        Palette.from(bitmap)
            .maximumColorCount(3)
            .clearFilters()
            .setRegion(bitmap.width - iconSize, 0, bitmap.width, iconSize)
            .generate { palette ->
                // Do something with the region palette.
                val lightness = ColorUtils.isDark(palette)
                val isDark = if (lightness == ColorUtils.LIGHTNESS_UNKNOWN) {
                    ColorUtils.isDark(bitmap, bitmap.width - iconSize / 2, iconSize / 2)
                } else {
                    lightness == ColorUtils.IS_DARK
                }


                if (isDark) { // make back icon dark on light images
                    favIcon.setImageResource(R.drawable.ic_hibin)
                } else {
                    favIcon.setImageResource(R.drawable.ic_hibin)
                }
            }
    }
}