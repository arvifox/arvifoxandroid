package com.arvifox.arvi.uicompose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

@Composable
fun ComposeChePre(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Fun1("vals")
    }
}

@Composable
private fun Fun1(s: String) {
    Column {
        Text(s)
        Text("sozdal $s")
    }
}

@Composable
private fun Fun2(s: String, cc: Color) {
    Column {
        Text(text = s, color = cc)
        Text("sozdal $s")
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(TextProvider::class) data: String,
) {
    Fun1(data)
}

class TextProvider : PreviewParameterProvider<String> {

    override val values: Sequence<String>
        get() = sequenceOf("odin", "dva", "tri")
}

data class Confi(val s: String, val c: Color)

fun interface Her {
    @Composable
    fun go(): Confi
}

private val set1 = Her {
    Confi("confi 1", Color.Red)
}

private val set2 = Her {
    Confi("confi 2", Color.Green)
}

class ConfiProvider : PreviewParameterProvider<Her> {
    override val values: Sequence<Her>
        get() = sequenceOf(set1, set2)
}

@Preview(showBackground = true)
@Composable
private fun Hlsjdf(
    @PreviewParameter(ConfiProvider::class) data: Her,
) {
    Fun2(data.go().s, data.go().c)
}