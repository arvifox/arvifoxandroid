package com.arvifox.fulltestcompose

import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal fun Modifier.clickableOnce(onClick: () -> Unit) = composed {
    val clickState = remember { mutableStateOf(false) }
    Modifier.clickable(onClick = {
        if (clickState.value.not()) {
            clickState.value = true
            onClick.invoke()
        }
    })
}

fun Modifier.debouncedClick(timeOut: Long = 300, onClick: () -> Unit) = composed(
    inspectorInfo = debugInspectorInfo {
        name = "debouncedClick"
        properties["timeOut"] = timeOut
        properties["onClick"] = onClick
    }
) {
    var isEnabled by rememberSaveable { mutableStateOf(true) }
    
    val coroutineScope = rememberCoroutineScope()
    val currentClickListener by rememberUpdatedState(onClick)

    Modifier.clickable(isEnabled) {
        coroutineScope.launch {
            isEnabled = false
            currentClickListener.invoke()
            delay(timeOut)
            isEnabled = true
        }
    }
}
