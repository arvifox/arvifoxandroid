package com.arvifox.arvi.uicompose.compose202508

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp

@Composable
fun Compose202508(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Box(
            Modifier.padding(start = 30.dp, top = 30.dp)
                .size(70.dp)
                .align(Alignment.Start)
                .dropShadow(
                    shape = RoundedCornerShape(20.dp),
                    shadow = Shadow(
                        15.dp,
                        color = Color.Blue,
                        spread = 10.dp,
                        alpha = 0.5f,
                    ),
                )
                .background(
                    Color.Green,
                    shape = RoundedCornerShape(20.dp),
                )
        )
        Box(
            Modifier.padding(end = 30.dp, top = 30.dp)
                .size(70.dp)
                .align(Alignment.End)
                .background(
                    Color.Green,
                    shape = RoundedCornerShape(20.dp),
                )
                .innerShadow(
                    shape = RoundedCornerShape(20.dp),
                    shadow = Shadow(
                        15.dp,
                        color = Color.Blue,
                        spread = 10.dp,
                        alpha = 0.5f,
                    ),
                )
        )
    }
}