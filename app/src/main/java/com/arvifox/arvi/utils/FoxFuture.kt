package com.arvifox.arvi.utils

import androidx.concurrent.futures.CallbackToFutureAdapter
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Future

object FoxFuture {

    fun getinfuture(): Future<String> {

    }

    fun getlist(): ListenableFuture<String> {
        return CallbackToFutureAdapter.getFuture()
    }
}