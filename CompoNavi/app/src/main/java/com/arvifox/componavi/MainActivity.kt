package com.arvifox.componavi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arvifox.componavi.ui.theme.CompoNaviTheme
import kotlinx.serialization.Serializable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CompoNaviTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = "start_dest"
                    ) {
                        composable(
                            route = "start_dest",
                        ) {
                            Button(
                                onClick = { navController.navigate(LogState(true)) }
                            ) {
                                Text("nav")
                            }
                        }
                        composable<LogState> {
                            val r: LogState = it.toRoute()

                        }
                    }
                }
            }
        }
    }
}

@Serializable
data class LogState(
    val boolean: Boolean,
)
