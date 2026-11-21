package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicReference
import kotlin.time.Duration
import kotlin.time.TimeMark
import kotlin.time.TimeSource

interface Invalidatable {

    /**
     * Делает значение неактуальным.
     */
    fun invalidate()
}

/**
 * Отличается от [AsyncLazyValue] тем, что сохранённое значение может устареть по прошествии [expireTime].
 *
 * @param load Параметр `isObsolete == true` указывает на то, что данные устарели и запрошены повторно,
 * а если `false`, то запрос данных новый.
 */
class ObsolescentAsyncLazyValue<T : Any>(
    private val timeSource: TimeSource,
    private val expireTime: Duration,
    private val masterScope: CoroutineScope,
    private val load: suspend (isObsolete: Boolean) -> T,
) : AsyncValue<T>, Invalidatable {

    private val deferredReference = AtomicReference<ObsolescentDeferred<T>?>()

    // Этот стейт всегда будет содержать последнее успешно скаченное значение.
    // При этом если кто-то заинтересуется свежим значением, оно будет тут обновлено.
    private val state = MutableStateFlow<T?>(null)
    private val flow = flow {
        get()
        state.collect {
            @Suppress("UnsafeCallOnNullableType")
            emit(it!!)
        }
    }

    override suspend fun get(): T {
        val oldDeferred: ObsolescentDeferred<T>? = deferredReference.get()
        val isObsolete = oldDeferred?.isObsolete() == true
        if (oldDeferred == null || isObsolete) {
            // Нас устраивает если много тредов параллельно создадут инстанс CompletableDeferred. Записать его всё равно сможет только один.
            val newDeferred = ObsolescentDeferred<T>(timeMark = timeSource.markNow() + expireTime)
            if (deferredReference.compareAndSet(oldDeferred, newDeferred)) {
                // Если мы тот самый тред, которому удалось записать новый Deferred, то мы же и запускаем загрузку новых данных.
                startLoadingAsync(newDeferred, isObsolete)
            }
        }
        @Suppress("UnsafeCallOnNullableType")
        return deferredReference.get()!!.await()
    }

    private fun startLoadingAsync(deferred: CompletableDeferred<T>, isObsolete: Boolean) {
        masterScope.launchUnsafe {
            // загрузку не прерываем отменой caller'а
            withContext(NonCancellable) {
                try {
                    val result = load(isObsolete)
                    state.value = result
                    deferred.complete(result)
                } catch (e: Exception) {
                    e.rethrowCancellation()
                    deferred.completeExceptionally(e)
                }
            }
        }
    }

    override fun observe(): Flow<T> = flow

    override fun getOrNull(): T? = state.value

    override fun invalidate() {
        deferredReference.set(null)
    }

    private class ObsolescentDeferred<T>(private val timeMark: TimeMark) :
        CompletableDeferred<T> by CompletableDeferred() {

        fun isObsolete() = isCancelled || timeMark.hasPassedNow()
    }
}