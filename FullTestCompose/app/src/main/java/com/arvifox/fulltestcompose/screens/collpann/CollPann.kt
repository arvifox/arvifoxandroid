package com.arvifox.fulltestcompose.screens.collpann

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import me.thekusch.composeview.CurrencyTextField

@Composable
internal fun CollPann() {
    Column {
        val s = remember { mutableStateOf("") }
        CurrencyTextField(
            onChange = {

            },
            currencySymbol = "$",
            initialText = s.value,
        )
        Button(onClick = {
            s.value = "123"
        }) {
            Text(text = "click")
        }
    }


    LaunchedEffect(key1 = Unit) {

    }

    SideEffect {

    }

    DisposableEffect(key1 = Unit) {
        onDispose {

        }
    }

    val a by produceState(initialValue = 0) {
        this.value = 2
        awaitDispose {

        }
    }

    val b = rememberCoroutineScope()

    val c by remember {
        derivedStateOf {
            123
        }
    }

    val d = snapshotFlow {
        111
    }

    val e by rememberUpdatedState(newValue = 999)

}
