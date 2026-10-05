package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class ExampleViolation {
}

suspend fun start() {
    withContext(SupervisorJob()){

    }
}

suspend fun doInParallel(list: List<String>) = supervisorScope {
    for (item in list) {
        launch { doItem(item) }
    }
}

private suspend fun doItem(item: String) {
    delay(1.seconds)
}

suspend fun getMoney(): String = coroutineScope {
    launch {
        delay(3.seconds)
    }
    ""
}