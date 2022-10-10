package com.arvifox.fulltestcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arvifox.fulltestcompose.io.buildSvgImage
import com.arvifox.fulltestcompose.ui.theme.FullTestComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FullTestComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
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
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = "Hello $name!",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        textAlign = TextAlign.Center
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FullTestComposeTheme {
        Box(
            modifier = Modifier
                .height(50.dp)
                .width(50.dp)
        ) {
            Blur()
        }
    }
}