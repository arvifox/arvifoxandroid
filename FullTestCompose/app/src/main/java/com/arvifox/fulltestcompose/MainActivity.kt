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
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.arvifox.fulltestcompose.io.buildSvgImage
import com.arvifox.fulltestcompose.screens.old.OldScreen
import com.arvifox.fulltestcompose.screens.shadow.ShadowScreen
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
                    val navController = rememberNavController()
                    NavHost(
                        modifier = Modifier.fillMaxSize(),
                        navController = navController,
                        startDestination = "start"
                    ) {
                        composable("old") {
                            OldScreen()
                        }
                        composable("shadow") {
                            ShadowScreen()
                        }
                        composable("start") {
                            Column {
                                Button(onClick = { navController.navigate("old") }) {
                                    Text(text = "route 01")
                                }
                                Button(onClick = { navController.navigate("shadow") }) {
                                    Text(text = "route 01")
                                }
                            }
                        }
                    }

                }
            }
        }
    }
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