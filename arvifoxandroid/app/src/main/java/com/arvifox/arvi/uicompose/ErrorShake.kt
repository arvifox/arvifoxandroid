package com.arvifox.arvi.uicompose

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

private const val RED_COLOR_ANIMATION_DURATION = 50
private const val RED_COLOR_DURATION = 300L
private const val CODE_OFFSET_ANIMATION_DURATION = 400

@Suppress("MagicNumber")
@Composable
internal fun rememberErrorShakeAnimatable(errorFlow: Flow<Unit>): Animatable<Float, AnimationVector1D> {
    val xOffset = remember { Animatable(0f) }
    val maxOffset = with(LocalDensity.current) { 5.dp.toPx() }
    LaunchedEffect(errorFlow) {
        errorFlow.collect {
            xOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = CODE_OFFSET_ANIMATION_DURATION
                    0f at 0 using FastOutLinearInEasing
                    maxOffset at 100 using LinearOutSlowInEasing
                    0f at 150 using FastOutLinearInEasing
                    maxOffset at 300 using LinearOutSlowInEasing
                    0f at 400 using FastOutLinearInEasing
                }
            )
        }
    }
    return xOffset
}

@Composable
internal fun rememberErrorTextColorAnimatable(errorFlow: Flow<Unit>, initialColor: Color): Animatable<Color, AnimationVector4D> {
    val errorColor = Color.Red
    val textColor = remember { Animatable(initialColor) }
    LaunchedEffect(errorFlow) {
        errorFlow.collect {
            textColor.animateTo(
                targetValue = errorColor,
                animationSpec = tween(durationMillis = RED_COLOR_ANIMATION_DURATION)
            )
            delay(RED_COLOR_DURATION)
            textColor.animateTo(
                targetValue = initialColor,
                animationSpec = tween(durationMillis = RED_COLOR_ANIMATION_DURATION)
            )
        }
    }
    return textColor
}

@Stable
internal data class TextFieldError(
    val xOffset: Animatable<Float, AnimationVector1D>,
    val textColor: Animatable<Color, AnimationVector4D>,
)