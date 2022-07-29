package com.arvifox.arvi.simplemisc.camera

import android.annotation.SuppressLint
import android.util.Size
import java.lang.Long.signum

import java.util.Comparator

internal class CompareSizesByArea : Comparator<Size> {

    @SuppressLint("NewApi")
    override fun compare(lhs: Size, rhs: Size) =
            signum(lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height)
}