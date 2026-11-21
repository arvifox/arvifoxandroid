package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * Assume [TimeoutCancellationException] as normal exception
 *
 * Handle [Deferred.await] while it cancelled
 *
 */
suspend inline fun Throwable.rethrowCancellation() {
    if (this is TimeoutCancellationException) {
        return
    }
    if (this !is CancellationException) {
        return
    }
    if (message == "DeferredCoroutine was cancelled") {
        coroutineContext.ensureActive()
        return
    }
    throw this
}

@Suppress("SuspendFunSwallowedCancellation")
inline fun CoroutineScope.launchSafe(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline onError: suspend (Exception) -> Unit,
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = launch(context, start) {
    try {
        block()
    } catch (e: Exception) {
        e.rethrowCancellation()
        try {
            onError(e)
        } catch (errorDeliveryException: Exception) {

        }
    }
}

inline fun CoroutineScope.launchSafe(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = launchSafe(
    context = context,
    start = start,
    onError = { e ->

    },
    block = block
)

/**
 * Call [CoroutineScope.launchUnsafe] if you are sure no exception will be thrown.
 * The difference between [CoroutineScope.launch] and [CoroutineScope.launchUnsafe] is that error
 * will be handled only by [globalUncaughtErrorHandler].
 * In case of a [kotlinx.coroutines.CancellationException], the coroutine completes normally by re-throwing the exception.
 *
 * Additionally, the Release Team will identify this issue faster and report it to you.
 */
@Suppress("SuspendFunSwallowedCancellation")
@DelicateCoroutinesApi
inline fun CoroutineScope.launchUnsafe(
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    crossinline block: suspend CoroutineScope.() -> Unit,
): Job = launch(context, start) {
    try {
        block()
    } catch (e: Throwable) {
        e.rethrowCancellation()
    }
}