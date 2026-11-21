package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import java.util.concurrent.atomic.AtomicReference

/**
 * Упрощенная альтернатива [Deferred]
 */
interface AsyncValue<out T> {

    /**
     * Возвращает актуальное значение.
     *
     * **Может бросить исключение.**
     */
    suspend fun get(): T

    /**
     * Возвращает значение в данный момент времени.
     * **Не запускает процесс получения значения!**
     *
     * Не может бросить исключение.
     */
    fun getOrNull(): T?

    /**
     * Позволяет подписаться на обновления значения.
     *
     * Производит актуальное значение при подписке как [kotlinx.coroutines.flow.StateFlow].
     *
     * **Может завершиться исключением.**
     */
    fun observe(): Flow<T>
}

/**
 * Используется для того, чтобы можно было параллельно получать
 * значения из разных [AsyncValue].
 */
fun <T> AsyncValue<T>.getAsync(coroutineScope: CoroutineScope): Deferred<T> = coroutineScope.async {
    get()
}

/**
 * Хранит значение, вычисленное функцией [load] до тех пор, пока не будет вызван [invalidate].
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SimpleValueCache<T>(
    private val masterScope: CoroutineScope,
    private val load: suspend () -> T,
) : AsyncValue<T>, Invalidatable {

    private val latestCacheFlow = MutableStateFlow<AsyncValue<T>>(AsyncLazyValue(masterScope = masterScope, load = load))
    private val latestValueFlow = latestCacheFlow.flatMapLatest { it.observe() }

    override fun observe(): Flow<T> = latestValueFlow

    fun observeLazyValues(): Flow<AsyncValue<T>> = latestCacheFlow

    override suspend fun get(): T = latestValueFlow.first()

    override fun getOrNull(): T? = latestCacheFlow.value.getOrNull()

    fun offerValue(value: T) {
        latestCacheFlow.tryEmit(FinalizedAsyncValue(value))
    }

    /**
     * Делает значение неактуальным.
     *
     * Если на значение есть подписчики, заставляет вычислить актуальное значение сразу.
     *
     * После вызова этого метода, [observe] произведёт новое значение или ошибку.
     */
    override fun invalidate() {
        latestCacheFlow.tryEmit(AsyncLazyValue(masterScope = masterScope, load = load))
    }
}

/**
 * AsyncValue с константным значением value
 */
class FinalizedAsyncValue<out T>(private val value: T) : AsyncValue<T> {

    override suspend fun get(): T = value

    override fun getOrNull(): T = value

    override fun observe(): Flow<T> = flowOf(value)
}


/**
 * Загружает данные один раз, потом возвращает закешированное значение.
 *
 * @param masterScope Задаёт скоуп, на котором будет выполняться [load]. Также этот скоуп
 * позволяет завершить [load] если вся задача больше не нужна.
 */
@Suppress("UnsafeCallOnNullableType")
class AsyncLazyValue<T>(
    masterScope: CoroutineScope,
    load: suspend () -> T,
) : AsyncValue<T> {

    @Suppress("VarCouldBeVal")
    @Volatile
    private var load: (suspend () -> T)? = load
    @Suppress("VarCouldBeVal")
    @Volatile
    private var masterScope: CoroutineScope? = masterScope
    private val deferredReference = AtomicReference<Deferred<T>?>()

    override suspend fun get(): T {
        val oldDeferred = deferredReference.get()
        if (oldDeferred == null || oldDeferred.isCancelled) {
            // Нас устраивает если много тредов параллельно создадут инстанс Deferred. Записать его всё равно сможет только один.
            val newDeferred = masterScope!!.async(start = CoroutineStart.LAZY) {
                val result = load!!()
                // Очищаем лямбду, которая потенциально может держать какие-то ссылки, тем самым экономим память.
                // FIXME: Временно отключаем эту оптимизацию до выяснения причин краша.
//                load = null
//                masterScope = null
                result
            }
            deferredReference.compareAndSet(oldDeferred, newDeferred)
        }

        return deferredReference.get()!!.await()
    }

    override fun observe(): Flow<T> = ::get.asFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOrNull(): T? = try {
        deferredReference.get()?.getCompleted()
    } catch (e: Exception) {
        null
    }
}
