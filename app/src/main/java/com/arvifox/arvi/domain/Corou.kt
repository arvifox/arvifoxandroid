package com.arvifox.arvi.domain

import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@FlowPreview
object Corou {
    val ints: Flow<Int> = flow {
        for (i in 1..10) {
            delay(100)
            emit(i) // <-- emit is called here
        }
    }
}