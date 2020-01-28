package com.arvifox.arvi.kotlinpu

import com.arvifox.arvi.domain.corou.qwefd
import kotlinx.coroutines.Dispatchers
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
}