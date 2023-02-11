package com.arvifox.fulltestcompose.screens.old

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arvifox.fulltestcompose.io.buildSvgImage

@Composable
internal fun OldScreen() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Greeting("Android")
        //Blur()
        AsyncImage(
            model = buildSvgImage(LocalContext.current),
            contentDescription = null,
            modifier = Modifier.size(42.dp),
        )
    }
}

@Composable
private fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        textAlign = TextAlign.Center
    )
}