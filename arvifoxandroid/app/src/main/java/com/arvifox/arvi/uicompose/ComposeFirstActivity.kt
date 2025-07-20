package com.arvifox.arvi.uicompose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
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
import coil3.compose.rememberAsyncImagePainter
import com.arvifox.arvi.domain.connection.AndroidConnectivity
import com.arvifox.arvi.domain.connection.Connectivity
import com.arvifox.arvi.uicompose.anima.Anima
import com.arvifox.arvi.uicompose.anima.AnimaScreen
import com.arvifox.arvi.uicompose.draw.DrawMainScree
import com.arvifox.arvi.uicompose.ui.ArvifoxandroidTheme
import com.arvifox.arvi.uicompose.vertpager.VertPager
import com.arvifox.arvi.uicompose.vertpager.VertPagerNav
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                        this.Anima(nhc)
                        this.VertPager(nhc)
                        composable("screenles") {
                            ScreenLes()
                        }
                        composable("comil") {
                            Comil()
                        }
                        composable("drava") {
                            DrawMainScree()
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
                                //ImaCom()
                                Box(
                                    modifier = Modifier
                                        .clickable(enabled = true, onClick = {})
                                        .draggable(
                                            orientation = Orientation.Horizontal,
                                            state = rememberDraggableState { delta -> },
                                        )
                                        .scrollable(
                                            orientation = Orientation.Horizontal,
                                            state = rememberScrollableState { delta -> delta },
                                        )
                                        .transformable(state = rememberTransformableState { a, b, c -> })
//                                        .combinedClickable()
                                        .pointerInput(Unit) {
                                            detectTapGestures(
                                                onDoubleTap = null,
                                                onLongPress = null,
                                                onPress = {

                                                },
                                                onTap = null,
                                            )
                                            detectDragGestures(
                                                onDragStart = {},
                                                onDragEnd = {},
                                                onDragCancel = {},
                                                onDrag = { change, dragAmount ->
                                                },
                                            )
//                                            detectTransformGestures {  }
//                                    detectDragGesturesAfterLongPress {  }
//                                    detectHorizontalDragGestures {  }
//                                    detectVerticalDragGestures {  }
                                        }
                                        .size(48.dp)
                                        .background(color = Color.Blue)) {
                                    Text("gesture")
                                }
                                Buro("btn 01", "btn 02", "btn 03") {
                                    when (it) {
                                        1 -> {
                                            nhc.navigate("screenles")
                                        }

                                        2 -> {
                                            nhc.navigate("comil")
                                        }

                                        else -> {
                                            nhc.navigate("drava")
                                        }
                                    }
                                }
                                Spacer(Modifier.size(4.dp))
                                Buro("aniuma", "vertpa", "btn 06") {
                                    when (it) {
                                        1 -> {
                                            nhc.navigate(route = AnimaScreen)
                                        }

                                        2 -> {
                                            nhc.navigate(route = VertPagerNav)
                                        }
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

@Composable
private fun ImaCom() {
    var offset by remember { mutableStateOf(Offset.Zero) }
    var zoom by remember { mutableFloatStateOf(1f) }
    Image(
        painter = rememberAsyncImagePainter("photo url"),
        contentDescription = null,
        modifier = Modifier
            .clipToBounds()
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        zoom = if (zoom > 1f) 1f else 2f
                        offset = Offset(64f, 64f)
                    },
                )
            }
            .graphicsLayer {
                translationX = -offset.x * zoom
                translationY = -offset.y * zoom
                scaleX = zoom
                scaleY = zoom
                transformOrigin = TransformOrigin(0f, 0f)
            }
            .aspectRatio(1f)
    )
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
//    private val cow = CopyOnWriteArrayList<Int>()
//    private val lhs = LinkedHashSet<Int>()
//    private val re = Result.success(987)
//
//    @OptIn(ExperimentalCoroutinesApi::class)
//    private val ml = cha.mapLatest {
//        "$it and $it"
//    }
//    val mss = cha.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5000),
////        started = SharingStarted.Lazily,
////        started = SharingStarted.Eagerly,
//        initialValue = "777",
//    )

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
