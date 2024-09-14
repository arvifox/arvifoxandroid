package com.arvifox.fulltestcompose.screens.collpann

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

@Composable
fun Composed(tran: List<Int> = listOf(12, 23, 34, 56, 67, 23, 567)) {
    Column(modifier = Modifier.fillMaxSize()) {
        var counter by remember { mutableIntStateOf(0) }
        var sha by remember { mutableStateOf(false) }
        val trs = remember(tran) {
            tran.runningReduce { acc, i -> acc + i }
        }
        for ((t, s) in tran.zip(trs)) {
            Text("t = $t, s = $s")
        }
        Text("prct = ${percentile(tran.sorted().map { it.toDouble() }.toTypedArray(), 0.1)}")
        Button(
            onClick = {
                counter++
                sha = sha.not()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Increase $counter")
        }
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .shake(sha)
                .background(Color.Red, CircleShape)
                .size(50.dp)
                .padding(10.dp)
        )
        Text(text = "composed")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Box(
                modifier = Modifier
                    .composedBackground(150.dp, 20.dp, 0)
                    .width(150.dp)
            ) {
                Text(text = "Recomposed $counter")
            }

            Box(
                modifier = Modifier
                    .composedBackground(150.dp, 20.dp, 1)
                    .width(150.dp)
            ) {
                Text(text = "Recomposed $counter")
            }
        }
        Text(text = "not composed")
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            Box(
                modifier = Modifier
                    .nonComposedBackground(150.dp, 20.dp)
                    .width(150.dp)
            ) {
                Text(text = "Recomposed $counter")
            }

            Box(
                modifier = Modifier
                    .nonComposedBackground(150.dp, 20.dp)
                    .width(150.dp)
            ) {
                Text(text = "Recomposed $counter")
            }
        }
    }
}

fun Modifier.composedBackground(width: Dp, height: Dp, index: Int) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "myModifier"
        properties["width"] = width
        properties["height"] = height
        properties["index"] = index
    },
    factory = {
        val density = LocalDensity.current
        val color: Color = remember(index) {
            Color(
                red = Random.nextInt(256),
                green = Random.nextInt(256),
                blue = Random.nextInt(256),
                alpha = 255
            )
        }
        Modifier.drawBehind {
            val widthInPx = with(density) { width.toPx() }
            val heightInPx = with(density) { height.toPx() }
            drawRect(color = color, topLeft = Offset.Zero, size = Size(widthInPx, heightInPx))
        }
    }
)

fun Modifier.nonComposedBackground(width: Dp, height: Dp) = this.then(
    Modifier.drawBehind {
        val color: Color = Color(
            red = Random.nextInt(256),
            green = Random.nextInt(256),
            blue = Random.nextInt(256),
            alpha = 255
        )
        val widthInPx = width.toPx()
        val heightInPx = height.toPx()

        drawRect(color = color, topLeft = Offset.Zero, size = Size(widthInPx, heightInPx))
    }
)

fun Modifier.shake(enabled: Boolean) = composed(
    factory = {
        val scale by animateFloatAsState(
            targetValue = if (enabled) .9f else 1f,
            animationSpec = infiniteRepeatable(
//                iterations = 8,
                animation = tween(durationMillis = 50, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = ""
        )
        Modifier.graphicsLayer {
            scaleX = if (enabled) scale else 1f
            scaleY = if (enabled) scale else 1f
        }
    },
    inspectorInfo = debugInspectorInfo {
        name = "shake"
        properties["enabled"] = enabled
    }
)

private fun percentile(arr: Array<Double>, lmt: Double): Double {
    val k = (arr.size - 1) * lmt
    val f = floor(k)
    val c = ceil(k)
    if (f == c) {
        return arr[k.toInt()]
    }
    val d0 = arr[f.toInt()] * (c-k)
    val d1 = arr[c.toInt()] * (k-f)
    return d0 + d1
}
