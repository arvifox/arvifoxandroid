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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arvifox.arvi.domain.connection.AndroidConnectivity
import com.arvifox.arvi.domain.connection.Connectivity
import com.arvifox.arvi.uicompose.ui.ArvifoxandroidTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.CopyOnWriteArrayList

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
                            val cvm = viewModel<ComposeFirstViewModel> {
                                ComposeFirstViewModel(
                                    connectivity = AndroidConnectivity(applicationContext)
                                )
                            }
                            val ch = cvm.cha.collectAsStateWithLifecycle("initial").value
                            val sss = cvm.ss.collectAsStateWithLifecycle().value
                            println("foxxx flow $ch $sss")
                            Column(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                            ) {
                                Text("connected ? = ${cvm.connected.collectAsStateWithLifecycle().value}")
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

class ComposeFirstViewModel(
    private val connectivity: Connectivity,
) : ViewModel() {

    val connected = connectivity.connected
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false,
        )

    private val channel = Channel<String>()
    val cha = channel.consumeAsFlow()
    private val cow = CopyOnWriteArrayList<Int>()
    private val lhs = LinkedHashSet<Int>()
    private val re = Result.success(987)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val ml = cha.mapLatest {
        "$it and $it"
    }
    val mss = cha.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
//        started = SharingStarted.Lazily,
//        started = SharingStarted.Eagerly,
        initialValue = "777",
    )

    val ss = MutableStateFlow(123)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            println("foxxx thread ${Thread.currentThread()}")
            withContext(Dispatchers.Default) {
                println("foxxx thread ${Thread.currentThread()}")
            }
        }
    }
}
