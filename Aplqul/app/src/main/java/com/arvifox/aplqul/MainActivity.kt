package com.arvifox.aplqul

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.apollographql.apollo3.api.Optional
import com.arvifox.aplqul.ui.theme.AplqulTheme
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

private fun yest() = (TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS) - 24 * 60 * 60).toInt()

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AplqulTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val s = rememberCoroutineScope()
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Text(text = "jahnem text")
                        Button(onClick = {
                            s.launch {
                                val q = GetAssetsInfoQuery(
                                    pageCount = 10,
                                    cursor = "",
                                    tokenIds = Optional.present(
                                        listOf(
                                            "0x0200000000000000000000000000000000000000000000000000000000000000",
                                            "0x0200040000000000000000000000000000000000000000000000000000000000",
                                            "0x0200050000000000000000000000000000000000000000000000000000000000",
                                            "0x0200060000000000000000000000000000000000000000000000000000000000",
                                            "0x0200070000000000000000000000000000000000000000000000000000000000",
                                            "0x0200080000000000000000000000000000000000000000000000000000000000",
                                            "0x00019b95ac2c945d339e748857b1610e72ae33db52221a9b96e15965afd67b8c",
                                        )
                                    ),
                                    timestamp = yest(),
                                )
                                val a: GetAssetsInfoQuery.Data? = DepBui.apl.query(q).execute().data
                                Log.e("foxx", "s = ${a?.entities?.nodes?.size}")
                            }
                        }) {
                            Text(text = "click")
                        }
                    }
                }
            }
        }
    }
}
