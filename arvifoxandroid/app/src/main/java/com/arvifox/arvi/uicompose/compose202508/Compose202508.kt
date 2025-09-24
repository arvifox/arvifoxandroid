package com.arvifox.arvi.uicompose.compose202508

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollable2DState
import androidx.compose.foundation.gestures.scrollable2D
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.layout.LazyLayoutCacheWindow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.OutputTransformation
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.insert
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.onFirstVisible
import androidx.compose.ui.layout.onVisibilityChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

/*
https://android-developers.googleblog.com/2025/08/whats-new-in-jetpack-compose-august-25-release.html?m=1
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun LazyColumnCacheWindowDemo() {
    // Prefetch items 150.dp ahead and retain items 100.dp behind the visible viewport
    val dpCacheWindow = LazyLayoutCacheWindow(ahead = 150.dp, behind = 100.dp)
    // Alternatively, prefetch/retain items as a fraction of the list size
    // val fractionCacheWindow = LazyLayoutCacheWindow(aheadFraction = 1f, behindFraction = 0.5f)
    val state = rememberLazyListState(cacheWindow = dpCacheWindow)
    LazyColumn(state = state) {
        items(1000) { Text(text = "$it", fontSize = 80.sp) }
    }
}

@Composable
private fun Csdfh() {
    val offset = remember { mutableStateOf(Offset.Zero) }
    Box(
        Modifier
            .size(150.dp)
            .scrollable2D(
                state =
                    rememberScrollable2DState { delta ->
                        offset.value = offset.value + delta // update the state
                        delta // indicate that we consumed all the pixels available
                    }
            )
            .background(Color.LightGray),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "X=${offset.value.x.roundToInt()} Y=${offset.value.y.roundToInt()}",
            style = TextStyle(fontSize = 32.sp),
        )
    }
}

@Composable
fun Compose202508(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
    ) {
        Box(
            Modifier
                .padding(start = 30.dp, top = 30.dp)
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
            Modifier
                .padding(end = 30.dp, top = 30.dp)
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
        // new Modifiers
        Text(
            text = "qwe",
            modifier = Modifier
                .size(12.dp)
                .onVisibilityChanged(minDurationMs = 500, minFractionVisible = 0.3f) { visible ->

                }
                .onFirstVisible(minDurationMs = 400) {},
        )

        // Format a phone number and color the punctuation
        val phoneTransformation = OutputTransformation {
            // 1234567890 -> (123) 456-7890
            if (length == 10) {
                insert(0, "(")
                insert(4, ") ")
                insert(9, "-")

                // Color the added punctuation
                val gray = Color(0xFF666666)
                addStyle(SpanStyle(color = gray), 0, 1)
                addStyle(SpanStyle(color = gray), 4, 5)
                addStyle(SpanStyle(color = gray), 9, 10)
            }
        }
        BasicTextField(
            state = TextFieldState(""),
            outputTransformation = phoneTransformation,
        )
    }
}