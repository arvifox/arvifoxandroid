package jp.co.soramitsu.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.co.soramitsu.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val vm = viewModel<MainViewModel>()
            val state = vm.davay(applicationContext).collectAsStateWithLifecycle()
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                            painter = painterResource(id = R.drawable.bg_image),
                            contentDescription = ""
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .statusBarsPadding()
                                .navigationBarsPadding()
                        ) {
                            Log.e("foxx", "state coll ${state.value}")
                            Text(text = "value = ${state.value}", color = Color.Red)
                        }
                        Text(
                            text = "BottoM TexT",
                            color = Color.Black,
                            fontSize = 36.sp,
                            modifier = Modifier
                                .wrapContentHeight()
                                .align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }
    }
}
