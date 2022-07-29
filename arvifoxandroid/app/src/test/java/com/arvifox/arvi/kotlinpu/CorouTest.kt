package com.arvifox.arvi.kotlinpu

import com.arvifox.arvi.domain.corou.CoroutExce
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CorouTest {

    @Test
    fun test01() {
        CoroutExce.qwer02()
        assertEquals(true, true)
    }

    /**
     * https://www.halfbit.de/posts/fairest-kotlin-coroutines-operator
     */
    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
    @Test
    fun testt() {
        runBlockingTest {
            val letterFlow = MutableSharedFlow<String>()
            val digitFlow = MutableSharedFlow<Int>()

            var actual = ""
            val job = launch {
                combine(letterFlow, digitFlow) { letter, digit -> letter + digit }
                    .collect {
                        actual += "$it "
                    }
            }

//            letterFlow.emit("A")
//            digitFlow.emit(1)
//            digitFlow.emit(2)
//            letterFlow.emit("B")
            letterFlow.fairEmit("A")
            digitFlow.fairEmit(1)
            digitFlow.fairEmit(2)
            letterFlow.fairEmit("B")
            advanceUntilIdle()

            assertEquals("A1 A2 B2 ", actual)

            job.cancelAndJoin()
        }
    }

    suspend fun <T> MutableSharedFlow<T>.fairEmit(value: T) {
        emit(value)
        yield()
    }
}