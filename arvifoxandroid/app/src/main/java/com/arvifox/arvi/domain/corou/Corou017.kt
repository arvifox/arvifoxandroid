package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.*

object qwefd {
    val job: Job = Job()
    val scope = CoroutineScope(Dispatchers.Unconfined + job)

    // may throw Exception
    fun doWork(): Deferred<String> = scope.async {
        println("bef exce")
//        throw IllegalStateException()
        "sdf"[6].toString()
    }

    fun loadData() = scope.launch {
        println("slajflsjf")
        try {
            println("222")
            val d = doWork().await()
            println("d = $d")
        } catch (e: Exception) {
            println("catch exce e ${e.localizedMessage}")
        }
    }
}

object qwefd2 {
    val job: Job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.Default + job)

    fun doWork(): Deferred<String> = scope.async {
        "sdf"
    }

    fun loadData() = scope.launch {
        try {
            doWork().await()
        } catch (e: Exception) {

        }
    }
}

object qwefd3 {
    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.Default + job)

    fun loadData() = scope.launch {
        try {
            async {
                // (1)
                // may throw Exception
            }.await()
        } catch (e: Exception) {

        }
    }
}

object qwefd4 {
    val job = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.Default + job)

    // may throw Exception
    suspend fun doWork(): Deferred<String> = coroutineScope {
        // (1)
        async {
            "sdf"
        }
    }

    fun loadData() = scope.launch {
        // (2)
        try {
            doWork().await()
        } catch (e: Exception) {

        }
    }
}