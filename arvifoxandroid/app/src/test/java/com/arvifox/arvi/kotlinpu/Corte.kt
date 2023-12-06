package com.arvifox.arvi.kotlinpu

import com.arvifox.arvi.domain.corou.qwefd
import com.arvifox.arvi.domain.inlinekotlin.isA
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test

class Corte {

    private val td = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(td)
    }

    @After
    fun teardown() {
        Dispatchers.resetMain()
    }

    @Test
    fun te01() {
        runBlockingTest {
            println("qwqwe")
            val d = qwefd.loadData()
        }
    }

    @Test
    fun qerwr() = runTest {
        val scopeJob = Job()
        val scope = CoroutineScope(scopeJob + Dispatchers.Default)
        val job1 = scope.launch {
            withContext(NonCancellable) {
                var itera = 1
                while (isActive && itera <= 5) {
                    println("itera $itera")
                    itera++
                    delay(50)
                }
            }
        }
        val job2 = scope.launch {
            delay(100)
            job1.cancel()
        }
        joinAll(job1, job2)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun erw() {
        runBlockingTest {
            val result = getLast()
            println("r=$result")
        }
    }

    private suspend fun getLast(): Int {
        return startfl().first { it == 6 }
    }

    private fun startfl(): Flow<Int> = flow {
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
}