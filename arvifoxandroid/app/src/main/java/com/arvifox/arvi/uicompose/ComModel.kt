package com.arvifox.arvi.uicompose

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
data class ComModel(
    val a: Int,
    val s: String,
)

@Immutable
data class ComImModel(
    val a: Int,
    val s: String,
)
