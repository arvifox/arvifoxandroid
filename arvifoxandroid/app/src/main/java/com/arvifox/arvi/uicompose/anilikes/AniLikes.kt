package com.arvifox.arvi.uicompose.anilikes

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Serializable
object AniLikesRoute

@Serializable
private object AniLikesScreen

fun NavGraphBuilder.aniLikes(nhc: NavHostController) {
    this.navigation<AniLikesRoute>(startDestination = AniLikesScreen) {
        composable<AniLikesScreen>() {
            val vm: AniLikesViewModel = viewModel()
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterEnd),
                ) {
                    Box(
                        modifier = Modifier.wrapContentSize(),
                    ) {
                        Icon(
                            painter = painterResource(android.R.drawable.star_on),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(60.dp)
                                .onGloballyPositioned {
                                    vm.setUpAni(it.positionInRoot().y)
                                }
                                .clickable(
                                    onClick = { vm.showIcon() },
                                ),
                        )

                        val state = vm.iconsState.collectAsStateWithLifecycle().value
                        state.likes.forEach { iconState ->
                            IcoAni(iconState)
                        }
                    }
                    Icon(
                        painter = painterResource(android.R.drawable.ic_menu_camera),
                        contentDescription = null,
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(60.dp)
                            .clickable(
                                onClick = { vm.showIcon() },
                            ),
                    )
                }
            }
        }
    }
}

@Composable
fun IcoAni(iconState: AniLikesViewModel.IconState) {
    Icon(
        imageVector = Icons.Filled.Done,
        contentDescription = "Icon ${iconState.id}",
        modifier = Modifier
            .offset {
                IntOffset(0, -iconState.ofy.floatValue.toInt())
            }
            .size(60.dp),
    )
}

@Composable
fun Anili(
    id: Int,
    targetY: Float,
) {
    var isAni by remember { mutableStateOf(false) }
    val yOffset by animateFloatAsState(
        label = "ani $id",
        targetValue = if (isAni) targetY else 0.0f,
        animationSpec = tween(durationMillis = 2000),
    )
    if (yOffset > targetY) {
        Icon(
            imageVector = Icons.Filled.Done, // ImageVector.vectorResource(R.drawable.ic_heart_white_60dp),
            contentDescription = "Icon $id",
            modifier = Modifier
                .offset {
                    IntOffset(0, yOffset.toInt())
                }
                .size(60.dp),
        )
    }
    LaunchedEffect(Unit) {
        delay(100)
        isAni = true
    }
}