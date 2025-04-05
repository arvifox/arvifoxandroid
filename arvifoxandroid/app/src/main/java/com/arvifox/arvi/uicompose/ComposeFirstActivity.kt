package com.arvifox.arvi.uicompose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arvifox.arvi.uicompose.ui.ArvifoxandroidTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ComposeFirstActivity : ComponentActivity() {
    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, ComposeFirstActivity::class.java)
        }
    }

    private val sco = CoroutineScope(Dispatchers.Default)

    override fun onPause() {
        Log.d("foxx", "onPause")
        super.onPause()
    }

    override fun onDestroy() {
        Log.d("foxx", "onDestroy")
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        Log.d("foxx", "onStart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("foxx", "onCreate ${savedInstanceState == null}")
        enableEdgeToEdge()
        setContent {
            ArvifoxandroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val nhc: NavHostController = rememberNavController()
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = nhc,
                        startDestination = "navStart",
                    ) {
                        composable("screenles") {
                            ScreenLes()
                        }
                        composable("comil") {
                            Comil()
                        }
                        composable("navStart") {
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                            ) {
                                Button(onClick = {
                                    sco.launch {
                                        for (i in 1..1000) {
                                            Log.d("foxx", "count $i;")
                                            delay(1000)
                                        }
                                    }
                                }) { Text("click") }
                                Buro("btn 01", "btn 02", "btn 03") {
                                    when (it) {
                                        1 -> {
                                            nhc.navigate("screenles")
                                        }

                                        2 -> {
                                            nhc.navigate("comil")
                                        }
                                        else -> {}
                                    }
                                }
                                Spacer(Modifier.size(4.dp))
                                Buro("btn 04", "btn 05", "btn 06") {
                                    when (it) {
                                        1 -> {}
                                        2 -> {}
                                        else -> {}
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Buro(
    s1: String,
    s2: String,
    s3: String,
    onClick: (Int) -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
    ) {
        Button(
            modifier =
                Modifier
                    .wrapContentHeight()
                    .weight(1f),
            onClick = { onClick.invoke(1) },
        ) {
            Text(s1)
        }
        Button(
            modifier =
                Modifier
                    .wrapContentHeight()
                    .weight(1f),
            onClick = { onClick.invoke(2) },
        ) {
            Text(s2)
        }
        Button(
            modifier =
                Modifier
                    .wrapContentHeight()
                    .weight(1f),
            onClick = { onClick.invoke(3) },
        ) {
            Text(s3)
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ArvifoxandroidTheme {
        Greeting("Android")
    }
}
