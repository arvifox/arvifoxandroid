package com.arvifox.arvi.simplemisc.rx

import com.arvifox.arvi.utils.Logger
import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import org.reactivestreams.Publisher
import java.util.concurrent.TimeUnit

object RxUtil {
    fun tryCompose() {
        Flowable.interval(5L, TimeUnit.SECONDS).compose(object : FlowableTransformer<Long, String> {
            override fun apply(upstream: Flowable<Long>): Publisher<String> {
                return upstream.map { it.toString() }
            }
        }).subscribe { v -> Logger.d { "res=$v" } }
    }
}