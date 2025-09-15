package com.arvifox.arvi.kotlinpu

import com.arvifox.arvi.domain.corou.qwefd
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.isActive
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test

class Corte {
    private val td = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(td)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun te01() = runTest {
        println("qwqwe")
        val d = qwefd.loadData()
    }

    @Test
    fun qesfrwr() = runTest {
        val scopeJob = Job()
        val scope = CoroutineScope(scopeJob + Dispatchers.Default)
        val job1 =
            scope.launch {
                withContext(NonCancellable) {
                    var itera = 1
                    while (isActive && itera <= 5) {
                        println("itera $itera")
                        itera++
                        delay(50)
                    }
                }
            }
        val job2 =
            scope.launch {
                delay(100)
                job1.cancel()
            }
        joinAll(job1, job2)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun erw() = runTest {
        val result = getLast()
        println("r=$result")
    }
}

private suspend fun getLast(): Int {
    return startfl().first { it == 6 }
}

private fun startfl(): Flow<Int> =
    flow {
        emit(1)
        delay(300)
        emit(2)
        emit(3)
        emit(4)
        emit(5)
        emit(6)
        emit(7)
        emit(8)
        emit(9)
        emit(10)
    }.onCompletion {
        println("complete")
    }
