package com.arvifox.arvi.uicompose.anima

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable

@Serializable
object AnimaScreen

@Serializable
object AnimaFirst

fun NavGraphBuilder.Anima(nhc: NavHostController) {
    this.navigation<AnimaScreen>(startDestination = AnimaFirst) {
        composable<AnimaFirst> {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                ProgressLine(0)
                ProgressLine(1)
                ProgressLine(2)
            }
        }
    }
}

@Composable
private fun ProgressLine(s: Int) {
    var animatedFloat by remember {
        mutableFloatStateOf(0f)
    }
    val ani = if (s == 0) 0f else if (s == 2) 1f else animateFloatAsState(
        targetValue = animatedFloat,
        animationSpec = tween(durationMillis = 9000, easing = LinearEasing),
    ).value
//    val ani by animateFloatAsState(
//        targetValue = animatedFloat,
//        animationSpec = tween(durationMillis = 9000, easing = LinearEasing),
//    )
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .height(8.dp),
        color = Color.Red,
        trackColor = Color.Blue.copy(alpha = 0.5f),
        progress = {
            ani
        },
    )
    LaunchedEffect(Unit) {
        animatedFloat = 1f
    }
}