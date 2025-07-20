package com.arvifox.arvi.uicompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay
import java.util.Locale

@Composable
fun textWithTimer(text: String, ttl: Long?): String {
    val timeCalc = remember { { ttlToTime(ttl) } }
    val minutes = remember { mutableIntStateOf(1) }
    val seconds = remember { mutableIntStateOf(1) }
    val keepGoing by remember {
        derivedStateOf {
            seconds.intValue > 0 || minutes.intValue > 0
        }
    }
    LaunchedEffect(keepGoing) {
        while (keepGoing) {
            val (m, s) = timeCalc()
            minutes.intValue = m
            seconds.intValue = s
            delay(1000)
        }
    }
    return "$text ${
        String.format(
            Locale.US,
            "%01d",
            minutes.intValue
        )
    }:${String.format(Locale.US, "%02d", seconds.intValue)}"
}

private fun ttlToTime(ttl: Long?): Pair<Int, Int> {
    if (ttl == null) {
        return Pair(0, 0)
    } else {
        val totalSeconds = (ttl - System.currentTimeMillis()) / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds - minutes * 60
        return Pair(minutes.toInt(), seconds.toInt())
    }
}