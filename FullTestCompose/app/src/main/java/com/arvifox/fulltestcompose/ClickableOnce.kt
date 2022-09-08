package com.arvifox.fulltestcompose

import androidx.compose.foundation.clickable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed

internal fun Modifier.clickableOnce(onClick: () -> Unit) = composed {
    val clickState = remember { mutableStateOf(false) }
    Modifier.clickable(onClick = {
        if (clickState.value.not()) {
            clickState.value = true
            onClick.invoke()
        }
    })
}
