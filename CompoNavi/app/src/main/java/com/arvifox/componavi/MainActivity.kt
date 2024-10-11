package com.arvifox.componavi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.arvifox.componavi.ui.theme.CompoNaviTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var navManager: NavManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnLifecycleEvent(
                onCreate = { println("foxx activity onCreate") },
                onStart = { println("foxx activity onStart") },
                onResume = { println("foxx activity onResume") },
                onPause = { println("foxx activity onPause") },
                onStop = { println("foxx activity onStop") },
                onDestroy = { println("foxx activity onDestroy") },
            )
            CompoNaviTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    val lo = LocalLifecycleOwner.current
                    LaunchedEffect(key1 = lo.lifecycle) {
                        lo.repeatOnLifecycle(Lifecycle.State.STARTED) {
                            navManager.navFlow.collect {
                                when (it) {
                                    is NavEvent.NavTo -> {
                                        navController.navigate(it.d)
                                    }

                                    NavEvent.Pop -> {
                                        navController.navigateUp()
                                    }

                                    is NavEvent.PopWithResult -> {
                                        navController.previousBackStackEntry?.savedStateHandle?.set(it.result.k, it.result.result)
                                        navController.popBackStack()
                                    }
                                }
                            }
                        }
                    }
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = Dest.Splash,
                    ) {
                        composable<Dest.Splash> {
                            val vm: SplashViewModel = hiltViewModel()
                            Button(
                                onClick = vm::onClick,
                            ) {
                                Text("nav to login")
                            }
                        }
                        composable<Dest.Login> {
                            val eke = it.savedStateHandle.getStateFlow("eke", "def").collectAsStateWithLifecycle().value
                            val vm: LoginViewModel = hiltViewModel()
                            Column {
                                val vl = vm.foo.collectAsStateWithLifecycle().value
                                Text("value = $vl")
                                Text("ee = $eke")
                                Button(
                                    onClick = vm::onClick,
                                ) {
                                    Text("nav to details")
                                }
                            }
                        }
                        composable<Dest.Detail> {
                            val arg = it.toRoute<Dest.Detail>()
                            val vm: DetailViewModel = hiltViewModel()
                            var entered by remember { mutableStateOf("") }
                            Column {
                                Text("passed arg = ${arg.arg}")
                                TextField(
                                    value = entered,
                                    onValueChange = { v -> entered = v },
                                )
                                Button(onClick = {
                                    vm.onClick(entered)
                                }) {
                                    Text("go back to login")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val navManager: NavManager,
    ssh: SavedStateHandle,
) : ViewModel() {
    init {
        val r = ssh.toRoute<Dest.Detail>()
        println("foxx ssh = ${r.arg}")
    }
    fun onClick(entered: String) {
        viewModelScope.launch {
            navManager.pushEvent(NavEvent.PopWithResult(NavEventResult("eke", entered)))
        }
    }
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val navManager: NavManager,
) : ViewModel() {
    fun onClick() {
        viewModelScope.launch {
            navManager.pushEvent(NavEvent.NavTo(Dest.Login))
        }
    }
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val navManager: NavManager,
) : ViewModel() {
    val foo = MutableStateFlow("foo")
    fun onClick() {
        viewModelScope.launch {
            navManager.pushEvent(NavEvent.NavTo(Dest.Detail("details foo value")))
        }
    }
}
