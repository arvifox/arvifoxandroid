package com.arvifox.arvi.uicompose

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.DecayAnimation
import androidx.compose.animation.core.FloatExponentialDecaySpec
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.arvifox.arvi.R
import kotlinx.coroutines.launch

private data class Fla(val s: String, val u: Int)

private val fl = Fla("s value", 123)

@Composable
private fun Drasosh() {
    val cs = rememberCoroutineScope()
    val trax = remember { Animatable(0f) }
    val dr =
        rememberDraggableState(
            onDelta = { dragAmount ->
                cs.launch {
                    trax.snapTo(trax.value + dragAmount)
                }
            },
        )
    Box(
        modifier =
            Modifier
                .background(color = Color.Blue)
                .height(48.dp)
                .fillMaxWidth()
                .draggable(dr, Orientation.Horizontal),
    ) {
        Box(
            modifier =
                Modifier
                    .graphicsLayer {
                        translationX = trax.value
                        val scale = lerp(1f, 0.8f, trax.value / 190f)
                        scaleX = scale
                        scaleY = scale
                    }
                    .size(40.dp)
                    .background(color = Color.Red),
        )
    }
}

@Composable
private fun Drasosha() {
    Box(
        modifier =
            Modifier
                .background(color = Color.Green)
                .wrapContentHeight()
                .fillMaxWidth(),
    ) {
        val bbb = 280f
        var dra by remember { mutableStateOf(false) }
        val cs = rememberCoroutineScope()
        val trax = remember { Animatable(0f) }
        trax.updateBounds(0f, bbb)
        val dr =
            rememberDraggableState(
                onDelta = { dragAmount ->
                    cs.launch {
                        trax.snapTo(trax.value + dragAmount)
                    }
                },
            )
        Box(
            modifier =
                Modifier
                    .height(38.dp)
                    .width(bbb.dp)
                    .background(Color.Cyan),
        )
        val decay = rememberSplineBasedDecay<Float>()
        Box(
            modifier =
                Modifier
                    .height(6.dp)
                    .width(220.dp)
                    .background(Color.Magenta),
        )
        Box(
            modifier =
                Modifier
                    .graphicsLayer {
                        translationX = trax.value
                        val scale = lerp(1f, 0.8f, trax.value / bbb)
                        scaleX = scale
                        scaleY = scale
                    }
                    .draggable(
                        state = dr,
                        orientation = Orientation.Horizontal,
                        onDragStopped = { velocity ->
                            val decayX = decay.calculateTargetValue(trax.value, velocity)
                            this.launch {
                                val targetX =
                                    if (decayX > bbb * 0.5) {
                                        bbb
                                    } else {
                                        0f
                                    }
                                val canReachTargetWithDecay =
                                    (decayX > targetX && targetX == bbb) || (decayX < targetX && targetX == 0f)
                                if (canReachTargetWithDecay) {
                                    trax.animateDecay(velocity, decay)
                                } else {
                                    trax.animateTo(targetX, initialVelocity = velocity)
                                }
                                dra = targetX != bbb
                            }
                        },
                    )
                    .background(color = Color.Blue),
        ) {
            Column(
                modifier =
                    Modifier
                        .wrapContentSize(),
            ) {
                Text("value 01")
                Text("value 02")
                Text("value 03")
            }
        }
    }
}

@Composable
fun ScreenLes(modifier: Modifier = Modifier) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(color = Color.Gray)
                .verticalScroll(state = rememberScrollState()),
    ) {
        Drasosh()
        Drasosha()
        Text("Screen Les")
        var flag by remember { mutableStateOf(fl) }
        Button(
            onClick = {
                flag = flag.copy(u = flag.u * -1)
            },
        ) { Text("flag inversion") }
        AnimatedContent(
            targetState = flag,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 300)) togetherWith
                    fadeOut(
                        animationSpec =
                            tween(
                                durationMillis = 300,
                            ),
                    ) using
                    SizeTransform { initialSize, targetSize ->
                        if (targetState.u > 100) {
                            keyframes {
                                IntSize(initialSize.width, initialSize.height) at 300
                                durationMillis = 500
                            }
                        } else {
                            keyframes {
                                IntSize(targetSize.width, targetSize.height) at 300
                                durationMillis = 500
                            }
                        }
                    }
            },
        ) { fla ->
            if (fla.u > 100) {
                Column {
                    Text("text > 100")
                    Image(
                        painter = painterResource(id = R.drawable.ic_heart_white_60dp),
                        contentDescription = null,
                    )
                }
            } else {
                Column {
                    Text("text <= 100")
                    Image(
                        painter = painterResource(id = R.drawable.ic_heart_white_60dp),
                        contentDescription = null,
                    )
                    Text(
                        "Super plus text and eat fresh cakes and drink mild wine \n Count Dracula is the title character of Bram Stoker's 1897 gothic horror novel Dracula.\nHe is considered the prototypical and archetypal vampire in subsequent works of fiction.",
                    )
                }
            }
        }

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        ) {
            Column(
                modifier =
                    Modifier
                        .wrapContentHeight()
                        .weight(1f),
            ) {
                Text(text = "ani crossfade", modifier = Modifier.padding(top = 10.dp))
                var cf by remember { mutableStateOf(Fla("fla", 12)) }
                Button(
                    onClick = {
                        cf = cf.copy(u = cf.u * -1)
                    },
                ) { Text("crossfade") }
                Crossfade(targetState = cf) {
                    if (it.u > 0) {
                        Text("Count Dracula is the title character of Bram Stoker's 1897 gothic horror novel Dracula.")
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_heart_white_60dp),
                            contentDescription = null,
                        )
                    }
                }
            }
            Column(
                modifier =
                    Modifier
                        .wrapContentHeight()
                        .weight(1f),
            ) {
                Text(text = "updatetran", modifier = Modifier.padding(top = 10.dp))
                var sta by remember { mutableStateOf(false) }
                Button(onClick = { sta = sta.not() }) { Text("rot + size") }
                val tr = aanni(sta)
                Image(
                    painter = painterResource(id = R.drawable.ic_heart_white_60dp),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .background(color = Color.Red)
                            .size(tr.first)
                            .rotate(tr.second),
                )
            }
        }

        Text(text = "ani cont size", modifier = Modifier.padding(top = 10.dp))
        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
        ) {
            var bo by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    bo = bo.not()
                },
            ) { Text("anim") }
            Text("Count Dracula is the title character of Bram Stoker's 1897 gothic horror novel Dracula.")
            if (bo) {
                Text("He is considered the prototypical and archetypal vampire in subsequent works of fiction.")
                Image(
                    painter = painterResource(id = R.drawable.ic_heart_white_60dp),
                    contentDescription = null,
                )
            }
        }

        Text(text = "ani rota", modifier = Modifier.padding(top = 20.dp))
        var animated by remember { mutableStateOf(false) }
        Button(onClick = { animated = animated.not() }) { Text("rota") }
        val rota = remember { Animatable(initialValue = 360f) }
        LaunchedEffect(animated) {
            rota.animateTo(
                targetValue = if (animated) 0f else 360f,
                animationSpec = tween(durationMillis = 1000),
            )
        }
        Image(
            painter = painterResource(id = R.drawable.ic_heart_white_60dp),
            contentDescription = null,
            modifier =
                Modifier
                    .background(color = Color.Red)
                    .graphicsLayer {
                        rotationY = rota.value
                    },
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
        ) {
            Column(
                modifier =
                    Modifier
                        .wrapContentHeight()
                        .weight(1f),
            ) {
                Text(text = "tarbase", modifier = Modifier.padding(top = 20.dp))
                val anim =
                    remember {
                        TargetBasedAnimation(
                            animationSpec = tween(durationMillis = 2000),
                            typeConverter = Float.VectorConverter,
                            initialValue = 20f,
                            targetValue = 80f,
                        )
                    }
                var playTime by remember { mutableLongStateOf(0L) }
                var animValue by remember { mutableIntStateOf(0) }
                LaunchedEffect(Unit) {
                    val startTime = withFrameNanos { it }
                    do {
                        playTime = withFrameNanos { it } - startTime
                        animValue = anim.getValueFromNanos(playTime).toInt()
                    } while (anim.isFinishedFromNanos(playTime).not())
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_heart_white_60dp),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .background(color = Color.Red)
                            .size(animValue.dp),
                )
            }
            Column(
                modifier =
                    Modifier
                        .wrapContentHeight()
                        .weight(1f),
            ) {
                Text(text = "decani", modifier = Modifier.padding(top = 20.dp))
                val anim =
                    remember {
                        DecayAnimation(
                            animationSpec = FloatExponentialDecaySpec(frictionMultiplier = 0.7f),
                            initialValue = 0f,
                            initialVelocity = 400f,
                        )
                    }
                var playTime by remember { mutableLongStateOf(0L) }
                var animValue by remember { mutableIntStateOf(0) }
                LaunchedEffect(Unit) {
                    val startTime = withFrameNanos { it }
                    do {
                        playTime = withFrameNanos { it } - startTime
                        animValue = anim.getValueFromNanos(playTime).toInt()
                    } while (anim.isFinishedFromNanos(playTime).not())
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_heart_white_60dp),
                    contentDescription = null,
                    modifier =
                        Modifier
                            .background(color = Color.Red)
                            .size(animValue.dp),
                )
            }
        }
    }
}

@Composable
private fun aanni(s: Boolean): Pair<Dp, Float> {
    val tran = updateTransition(s, "")
    val size by tran.animateDp(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "",
    ) { ss ->
        if (ss) 136.dp else 56.dp
    }
    val rota by tran.animateFloat(
        transitionSpec = { tween(durationMillis = 1000) },
        label = "",
    ) { ss ->
        if (ss) 0f else 360f
    }
    return size to rota
}
