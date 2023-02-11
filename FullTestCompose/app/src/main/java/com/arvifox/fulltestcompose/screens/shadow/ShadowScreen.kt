package com.arvifox.fulltestcompose.screens.shadow

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.arvifox.fulltestcompose.R

@Composable
internal fun ShadowScreen() {
    Image(
        modifier = Modifier
            .size(50.dp)
            .shadow(
                elevation = 5.dp,
                shape = GenericShape { size, _ ->
                    moveTo(0f, 0f)
                    relativeLineTo(0f, size.height)
                    relativeLineTo(size.width, -size.height / 2f)
                    close()
                },
            ),
        painter = painterResource(
            id = R.drawable.news_photo
        ),
        contentDescription = null,
    )
}
