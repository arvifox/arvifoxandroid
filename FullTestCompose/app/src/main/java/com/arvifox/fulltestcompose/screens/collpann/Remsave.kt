package com.arvifox.fulltestcompose.screens.collpann

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

@Composable
fun Remsave() {
    Column {
        var dynamicData by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            delay(3000L)
            dynamicData = "New Text"
        }
        Remsavein(title = dynamicData)
    }
}

@Composable
fun Remsavein(title: String) {
    var data by remember { mutableStateOf("") }

    val updatedData by rememberUpdatedState(title)

    LaunchedEffect(Unit) {
        delay(5000L)
        data = updatedData
    }
    Text(text = data)
}
