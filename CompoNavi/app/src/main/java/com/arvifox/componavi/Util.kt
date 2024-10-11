package com.arvifox.componavi

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun OnLifecycleEvent(
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {},
) {
    val onCreateState by rememberUpdatedState(onCreate)
    val onStartState by rememberUpdatedState(onStart)
    val onResumeState by rememberUpdatedState(onResume)
    val onPauseState by rememberUpdatedState(onPause)
    val onStopState by rememberUpdatedState(onStop)
    val onDestroyState by rememberUpdatedState(onDestroy)

    val lifecycleOwner = rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner.value) {
        val lifecycle = lifecycleOwner.value.lifecycle
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> onCreateState()
                Lifecycle.Event.ON_START -> onStartState()
                Lifecycle.Event.ON_RESUME -> onResumeState()
                Lifecycle.Event.ON_PAUSE -> onPauseState()
                Lifecycle.Event.ON_STOP -> onStopState()
                Lifecycle.Event.ON_DESTROY -> onDestroyState()
                else -> {}
            }
        }

        lifecycle.addObserver(observer)
        onDispose {
            lifecycle.removeObserver(observer)
        }
    }
}

@Composable
fun OnStart(onStart: () -> Unit) {
    OnLifecycleEvent(onStart = onStart)
}

@Composable
fun OnBackPressed(onBackPressed: () -> Unit) {
    val currentOnClick by rememberUpdatedState(onBackPressed)
    val backPressListener = remember {
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() = currentOnClick()
        }
    }
    val backPressDispatcher = LocalOnBackPressedDispatcherOwner.current
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(backPressDispatcher, lifecycleOwner) {
        backPressDispatcher?.onBackPressedDispatcher?.addCallback(lifecycleOwner, backPressListener)
        onDispose {
            backPressListener.remove()
        }
    }
}