package com.arvifox.arvi.uicompose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

/*
https://www.youtube.com/watch?v=xcfEQO0k_gU
goo.gle/compose-jetlagged
goo.gle/compose-custom-layouts
goo.gle/compose-graphics-docs
 */

@Composable
fun FoxcusLayout(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit,
    label: @Composable (Int) -> Unit,
    post: @Composable (Int) -> Unit,
) {
    val labels = @Composable { repeat(3) { label(it) } }
    val posts = @Composable { repeat(3) { post(it) } }
    Layout(
        contents = listOf(header, labels, posts),
        modifier = modifier,
    ) { (h, l, p), constraints ->
        // 1. Measurement step
        val hPlaceable = h.first().measure(constraints)
        val lPlaceables = l.map { it.measure(constraints) }

        // 2. Placement step
        layout(123, 123) {

        }
    }
}