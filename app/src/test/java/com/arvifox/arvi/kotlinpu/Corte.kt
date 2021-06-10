package com.arvifox.arvi.kotlinpu

import com.arvifox.arvi.domain.corou.qwefd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
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