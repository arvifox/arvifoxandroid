package com.arvifox.arvi.uicompose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import coil3.imageLoader

@Composable
fun Comil(modifier: Modifier = Modifier) {
    Column(modifier) {
        AsyncImage(
            model = "https://www.hibiny.ru/images/news/2025/403732/5c5152df033ed41cb5090f728bb89f14.webp",
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f)
        )
        SubcomposeAsyncImage(
            model = "https://www.hibiny.ru/images/news/2025/403732/5c5152df033ed41cb5090f728bb89f14.webp",
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.4f),
            loading = {
            },
        )
        val cc = LocalContext.current
        Button(
            onClick = { cc.imageLoader.memoryCache?.clear() },
        ) { Text("clear") }
    }
}
