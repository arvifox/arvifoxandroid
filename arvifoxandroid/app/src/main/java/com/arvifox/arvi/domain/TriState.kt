package com.arvifox.arvi.domain

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
@Suppress("TooManyFunctions")
sealed class TriState<out T> {

    companion object {
        class NotSuccessException(val triState: TriState<*>) : Exception()

        operator fun <T> TriState<T>.component1(): T {
            return if (this is Success) {
                value
            } else {
                throw NotSuccessException(this)
            }
        }

        /**
         * Возвращает [Success] только если при обращении ко всем стейтам
         * внутри этой скоуп функции вернулся [Success].
         *
         * Пример:
         * ```
         * val state1 = TriState.Success(0)
         * val state2 = TriState.Success("hello")
         * val result: TriState<String> = TriState.successContext {
         *     val (value1) = state1
         *     val (value2) = state2
         *     value2 + value1
         * }
         * ```
         *
         * В result будет лежать [Success] если state1 и state2 оба [Success]
         */
        inline fun <R> successContext(body: Companion.() -> R): TriState<R> {
            try {
                return Success(this.body())
            } catch (e: NotSuccessException) {
                @Suppress("UNCHECKED_CAST")
                return e.triState as TriState<R>
            }
        }
    }

    @Immutable
    class Progress<T> : TriState<T>() {

        @Suppress("UNCHECKED_CAST")
        override fun asUnit(): Progress<Unit> = this as Progress<Unit>

        override fun toString(): String {
            return "Progress"
        }

        override fun equals(other: Any?): Boolean {
            return other is Progress<*>
        }

        override fun hashCode(): Int {
            return 0
        }
    }

    @Immutable
    data class Success<T>(val value: T) : TriState<T>() {

        override fun asUnit(): Success<Unit> = Success(Unit)
    }

    @Immutable
    data class Error<T>(val error: Exception) : TriState<T>() {

        @Suppress("UNCHECKED_CAST")
        override fun asUnit(): Error<Unit> = this as Error<Unit>
    }

    abstract fun asUnit(): TriState<Unit>
}

@Stable
internal val <T> TriState<T>.valueOrNull get() = (this as? TriState.Success)?.value

internal fun <T : Any> TriState<T>.success(): T? = (this as? TriState.Success)?.value

internal fun <T> TriState<T>.requireSuccess(): T = (this as TriState.Success).value

internal inline fun <T, R> TriState<T>.mapSuccess(mapper: (T) -> R): TriState<R> {
    return if (this is TriState.Success) {
        TriState.Success(mapper(value))
    } else {
        @Suppress("UNCHECKED_CAST")
        this as TriState<R>
    }
}

internal inline fun <T, R> TriState<T>.flatMapSuccess(mapper: (T) -> TriState<R>): TriState<R> {
    return if (this is TriState.Success) {
        mapper(value)
    } else {
        @Suppress("UNCHECKED_CAST")
        this as TriState<R>
    }
}

internal operator fun <A> TriState<A>.plus(b: TriState<A>): TriState<A> = flatMapSuccess { b }

/**
 * Пример:
 * `flowOf(flowOf(1), flow(2)).toTriStateFlow()`
 *
 * Превращается в:
 * `[Progress, Success(1), Progress, Success(2)]`
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal fun <T> Flow<Flow<T>>.toTriStateFlow(): Flow<TriState<T>> {
    return flatMapLatest {
        it.asTriStateFlow()
    }
}

/**
 * Пример:
 * ```
 * flowOf(1, 2, 3, 4).map { if (it == 3) throw RuntimeException() else it }.asTriStateFlow().toList()
 * ```
 *
 * Превращается в:
 * ```
 * [Progress, Success(value=1), Success(value=2), Error(error=java.lang.RuntimeException)]
 * ```
 */
internal fun <T> Flow<T>.asTriStateFlow(): Flow<TriState<T>> {
    return transform<T, TriState<T>> {
        emit(TriState.Success(it))
    }.catch {
        emit(TriState.Error(it as Exception))
    }.onStart {
        emit(TriState.Progress())
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
@JvmName("toTriStateFlowDeferred")
internal fun <T> Flow<Deferred<T>>.toTriStateFlow(): Flow<TriState<T>> {
    return flatMapLatest {
        it.asTriStateFlow()
    }
}

/**
 * Аналог: `asFlow().asTriStateFlow()`
 */
internal fun <T> Deferred<T>.asTriStateFlow(): Flow<TriState<T>> = flow {
    emit(TriState.Progress())

    try {
        val value = await()
        emit(TriState.Success(value))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        emit(TriState.Error<T>(e))
    }
}

internal fun <T> Flow<TriState<T>>.onTriSuccess(onSuccess: (T) -> Unit): Flow<TriState<T>> {
    return onEach {
        if (it is TriState.Success) {
            onSuccess(it.value)
        }
    }
}

internal fun <T> Flow<TriState<T>>.onTriError(onError: (Exception) -> Unit): Flow<TriState<T>> {
    return onEach {
        if (it is TriState.Error) {
            onError(it.error)
        }
    }
}

internal fun <T> Flow<TriState<T>>.collectTriTo(scope: CoroutineScope, onState: (TriState<T>) -> Unit): Job {
    return scope.launch {
        collect {
            onState(it)
        }
    }
}

internal fun Flow<TriState<*>>.collectTriToUnit(scope: CoroutineScope, onState: (TriState<Unit>) -> Unit): Job {
    return scope.launch {
        collect {
            onState(it.asUnit())
        }
    }
}

internal fun CoroutineScope.loadScreen(onState: (TriState<Unit>) -> Unit, body: suspend () -> Unit): Job {
    return triStateFlow(body).collectTriTo(scope = this, onState = onState)
}

/**
 * Аналог `suspend { ... }.asFlow().asTriStateFlow()`
 */
internal fun <T> triStateFlow(body: suspend () -> T): Flow<TriState<T>> {
    return flow {
        emit(TriState.Progress())

        try {
            emit(TriState.Success(body()))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emit(TriState.Error<T>(e))
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
internal fun <T, R> Flow<T>.switchMapTri(operation: suspend (T) -> Flow<R>): Flow<Pair<T, TriState<R>>> {
    return flatMapLatest { request ->
        operation(request).asTriStateFlow().map { request to it }
    }
}

internal inline fun TriState<*>.doIfProgress(action: () -> Unit) {
    if (this is TriState.Progress) action.invoke()
}

internal inline fun TriState<*>.doIfError(action: (Exception) -> Unit) {
    if (this is TriState.Error) action.invoke(error)
}

internal inline fun <T> TriState<T>.doIfSuccess(action: (T) -> Unit) {
    if (this is TriState.Success) action.invoke(this.value)
}