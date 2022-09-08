package com.arvifox.fulltestcompose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun Blur() {
    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(10.dp))
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                Modifier
                    .blur(8.dp, edgeTreatment = BlurredEdgeTreatment(RoundedCornerShape(10.dp)))
                    .clip(
                        RoundedCornerShape(10.dp)
                    ),
                contentScale = ContentScale.Fit,
            )
            Spacer(modifier = Modifier.height(20.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = null,
                Modifier.clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Fit
            )
        }
    }
}
