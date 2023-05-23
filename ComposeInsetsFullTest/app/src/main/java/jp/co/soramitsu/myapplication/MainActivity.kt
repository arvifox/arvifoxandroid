package jp.co.soramitsu.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import jp.co.soramitsu.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val vm = viewModel<MainViewModel>()
            val state = vm.davay(applicationContext).collectAsStateWithLifecycle()
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                            painter = painterResource(id = R.drawable.bg_image),
                            contentDescription = ""
                        )
                        Column(modifier = Modifier.fillMaxSize()) {
                            Log.e("foxx", "state coll ${state.value}")
                            Text(text = "value = ${state.value}")
                        }
                    }
                }
            }
        }
    }
}
