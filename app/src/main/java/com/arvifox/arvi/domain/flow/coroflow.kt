package com.arvifox.arvi.domain.flow

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

object dfff {

    @Parcelize
    class Gfdf(val df: Int) : Parcelable

    interface Fff {
        fun getFlowData(g: Int): Flow<Gfdf>
    }

    val oneElementFlow: Flow<Int> = flow {
        // producer block starts here, stream starts
        emit(1)
        // producer block finishes here, stream will be closed
    }

    val unlimitedElementFlow: Flow<Int> = flow {
        // producer block starts here, stream starts
        while (true) {
            // Do calculations
            emit(123)
            delay(100)
        }
        // producer block finishes here, stream will be closed
    }

}