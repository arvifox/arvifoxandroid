package com.arvifox.arvi.domain.corou

import android.content.SharedPreferences
import android.util.Log
import com.arvifox.arvi.domain.TriState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlin.concurrent.atomics.AtomicBoolean
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

/**
 * Возвращает результат выполнения асинхронной функции или [defaultValue], если произошла ошибка
 */
suspend fun <T> Deferred<T>.awaitOrElse(defaultValue: T): T {
    return try {
        await()
    } catch (e: Exception) {
        defaultValue
    }
}

/**
 * Возвращает результат выполнения асинхронной функции если функция завершена или [defaultValue], если функция еще не завершена
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Deferred<T>.getCompletedOrDefault(
    defaultValue: T,
    ifCompleted: () -> Unit = {},
): T {
    return if (isCompleted) {
        ifCompleted()
        getCompleted()
    } else {
        defaultValue
    }
}

inline fun <T1, T2, T3, T4, T5, T6, R> combine6(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6) -> R,
): Flow<R> = combine(flow, flow2, flow3, flow4, flow5, flow6) { args: Array<*> ->
    @Suppress("UNCHECKED_CAST")
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6
    )
}

inline fun <T1, T2, T3, T4, T5, T6, T7, R> combine7(
    flow: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7) -> R,
): Flow<R> = combine(flow, flow2, flow3, flow4, flow5, flow6, flow7) { args: Array<*> ->
    @Suppress("UNCHECKED_CAST")
    transform(
        args[0] as T1,
        args[1] as T2,
        args[2] as T3,
        args[3] as T4,
        args[4] as T5,
        args[5] as T6,
        args[6] as T7
    )
}

/**
 * При включенном флаге isMinifyEnabled на некоторых андроидах (точно на Андроид 7.1.1 на устройстве Sony H4311)
 * этот метод как-то оптимизируется и не работает. Отключение оптимизаций в proguard-rules и добавление этого метода
 * или файла в исключения не помогает.
 * combine7 при этом работает исправно
 **/
inline fun <T1, T2, T3, T4, T5, T6, T7, T8, R> combine8(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 8 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, ::Pair),
    ) { (value1, value2, value3), (value4, value5, value6), (value7, value8) ->
        transform(value1, value2, value3, value4, value5, value6, value7, value8)
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, R> combine9(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 9 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
    ) { (value1, value2, value3), (value4, value5, value6), (value7, value8, value9) ->
        transform(value1, value2, value3, value4, value5, value6, value7, value8, value9)
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R> combine10(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 10 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        flow10
    ) { (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), value10 ->
        transform(value1, value2, value3, value4, value5, value6, value7, value8, value9, value10)
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R> combine11(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 11 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, ::Pair),
    ) { (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), (value10, value11) ->
        transform(
            value1,
            value2,
            value3,
            value4,
            value5,
            value6,
            value7,
            value8,
            value9,
            value10,
            value11
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R> combine12(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 12 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, flow12, ::Triple),
    ) { (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), (value10, value11, value12) ->
        transform(
            value1,
            value2,
            value3,
            value4,
            value5,
            value6,
            value7,
            value8,
            value9,
            value10,
            value11,
            value12
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R> combine13(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    flow13: Flow<T13>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 13 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, flow12, ::Triple),
        flow13,
    ) { (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), (value10, value11, value12), value13 ->
        transform(
            value1,
            value2,
            value3,
            value4,
            value5,
            value6,
            value7,
            value8,
            value9,
            value10,
            value11,
            value12,
            value13
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R> combine14(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    flow13: Flow<T13>,
    flow14: Flow<T14>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 14 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, flow12, ::Triple),
        combine(flow13, flow14, ::Pair),
    ) { (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), (value10, value11, value12), (value13, value14) ->
        transform(
            value1,
            value2,
            value3,
            value4,
            value5,
            value6,
            value7,
            value8,
            value9,
            value10,
            value11,
            value12,
            value13,
            value14
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R> combine15(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    flow13: Flow<T13>,
    flow14: Flow<T14>,
    flow15: Flow<T15>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 15 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, flow12, ::Triple),
        combine(flow13, flow14, flow15, ::Triple),
    ) {
            (value1, value2, value3), (value4, value5, value6), (value7, value8, value9),
            (value10, value11, value12), (value13, value14, value15),
        ->
        transform(
            value1, value2, value3, value4, value5, value6, value7, value8, value9,
            value10, value11, value12, value13, value14, value15
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R> combine16(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    flow13: Flow<T13>,
    flow14: Flow<T14>,
    flow15: Flow<T15>,
    flow16: Flow<T16>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 16 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine6(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, flow12, ::Triple),
        combine(flow13, flow14, flow15, ::Triple),
        flow16,
    ) {
            (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), (value10, value11, value12),
            (value13, value14, value15), value16,
        ->
        transform(
            value1,
            value2,
            value3,
            value4,
            value5,
            value6,
            value7,
            value8,
            value9,
            value10,
            value11,
            value12,
            value13,
            value14,
            value15,
            value16,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R> combine17(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    flow13: Flow<T13>,
    flow14: Flow<T14>,
    flow15: Flow<T15>,
    flow16: Flow<T16>,
    flow17: Flow<T17>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 17 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine6(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, flow12, ::Triple),
        combine(flow13, flow14, flow15, ::Triple),
        combine(flow16, flow17, ::Pair),
    ) {
            (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), (value10, value11, value12),
            (value13, value14, value15), (value16, value17),
        ->
        transform(
            value1,
            value2,
            value3,
            value4,
            value5,
            value6,
            value7,
            value8,
            value9,
            value10,
            value11,
            value12,
            value13,
            value14,
            value15,
            value16,
            value17,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R> combine18(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    flow13: Flow<T13>,
    flow14: Flow<T14>,
    flow15: Flow<T15>,
    flow16: Flow<T16>,
    flow17: Flow<T17>,
    flow18: Flow<T18>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 18 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine6(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, flow12, ::Triple),
        combine(flow13, flow14, flow15, ::Triple),
        combine(flow16, flow17, flow18, ::Triple),
    ) {
            (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), (value10, value11, value12),
            (value13, value14, value15), (value16, value17, value18),
        ->
        transform(
            value1,
            value2,
            value3,
            value4,
            value5,
            value6,
            value7,
            value8,
            value9,
            value10,
            value11,
            value12,
            value13,
            value14,
            value15,
            value16,
            value17,
            value18,
        )
    }
}

inline fun <T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R> combine19(
    flow1: Flow<T1>,
    flow2: Flow<T2>,
    flow3: Flow<T3>,
    flow4: Flow<T4>,
    flow5: Flow<T5>,
    flow6: Flow<T6>,
    flow7: Flow<T7>,
    flow8: Flow<T8>,
    flow9: Flow<T9>,
    flow10: Flow<T10>,
    flow11: Flow<T11>,
    flow12: Flow<T12>,
    flow13: Flow<T13>,
    flow14: Flow<T14>,
    flow15: Flow<T15>,
    flow16: Flow<T16>,
    flow17: Flow<T17>,
    flow18: Flow<T18>,
    flow19: Flow<T19>,
    crossinline transform: suspend (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) -> R,
): Flow<R> {
    // Ходят легенды, что, если объединить все 18 Flow в одном combine через Array, оно сломается при обфускации
    // Поэтому делаем двухуровневую композицию
    return combine7(
        combine(flow1, flow2, flow3, ::Triple),
        combine(flow4, flow5, flow6, ::Triple),
        combine(flow7, flow8, flow9, ::Triple),
        combine(flow10, flow11, flow12, ::Triple),
        combine(flow13, flow14, flow15, ::Triple),
        combine(flow16, flow17, flow18, ::Triple),
        flow19,
    ) {
            (value1, value2, value3), (value4, value5, value6), (value7, value8, value9), (value10, value11, value12),
            (value13, value14, value15), (value16, value17, value18), value19,
        ->
        transform(
            value1,
            value2,
            value3,
            value4,
            value5,
            value6,
            value7,
            value8,
            value9,
            value10,
            value11,
            value12,
            value13,
            value14,
            value15,
            value16,
            value17,
            value18,
            value19,
        )
    }
}

/**
 * Проверить, что флоу не завис и произвел хотя бы один эмит в рамках заданного промежутка времени
 * Предполагается добавлять к тем флоу, значение которых получаем не сразу,
 * но которые согласно бизнес логике должны гарантированно заэмитить какой то результат
 *
 * Данная функция поможет формировать метрики по зависшим флоу за счет отправки нон-фаталов в аналитику
 * По дефолту специально выставлено 30 секунд, свыше этого времени в рамках текущих метрик считается, что флоу работает неправильно
 */
@OptIn(ExperimentalAtomicApi::class)
fun <T> Flow<T>.verifyFirstEmission(
    tag: String,
    timeoutMillis: Long = 30_000L,
): Flow<T> = flow {
    coroutineScope {
        val firstEmissionSeen = AtomicBoolean(false)

        val watchdog = launchSafe(onError = {}) {
            delay(timeoutMillis.milliseconds)
            if (!firstEmissionSeen.load()) {
                Log.d("", "")
            }
        }

        collect { value ->
            if (firstEmissionSeen.compareAndSet(expectedValue = false, newValue = true)) {
                watchdog.cancel()
            }
            emit(value)
        }
    }
}

/**
 * Создаёт [Flow], предназначенный для расшаривания стейта.
 *
 * В отличие от [SharedFlow] не замедляет работу апстима т.к. буфер обладает conflate поведением.
 *
 * Хелпер-функция, которая выбирает нужный тип conflatedShareX в зависимости от параметров.
 *
 * | Name            | Errors | distinct | conflate | Initial Value |
 * | --------------- | ------ | -------- | -------- | ------------- |
 * | conflatedShare  | cfg    | cfg      | yes      | no            |
 * | stateIn         | crash  | yes      | yes      | yes           |
 * | shareIn         | crash  | no       | no       | no            |
 *
 * @param passErrors - если true, то ошибки будут передаваться подписчикам, иначе будут завершать scope.
 * @param distinct - если true, то элементы будут проверяться на равенство с помощью equals.
 * @param stopTimeoutMillis - время ожидания перед остановкой, если нет подписчиков.
 */
fun <T> Flow<T>.conflatedShare(
    scope: CoroutineScope,
    passErrors: Boolean = true,
    distinct: Boolean = false,
    stopTimeoutMillis: Long = 1000,
): Flow<T> {
    return if (passErrors) {
        if (distinct) {
            conflatedSharePassDistinct(scope, stopTimeoutMillis)
        } else {
            conflatedSharePass(scope, stopTimeoutMillis)
        }
    } else {
        @Suppress("DEPRECATION")
        if (distinct) {
            conflatedShare3(scope, stopTimeoutMillis)
        } else {
            conflatedShare2(scope, stopTimeoutMillis)
        }
    }
}

private fun <T> Flow<T>.conflatedSharePassDistinct(
    scope: CoroutineScope,
    stopTimeoutMillis: Long
): Flow<T> {
    return this
        .wrapWithTriState()
        .stateIn(
            scope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = stopTimeoutMillis,
                replayExpirationMillis = 0
            ),
            initialValue = TriState.Progress()
        )
        .transform {
            when (it) {
                is TriState.Success -> emit(it.value)
                is TriState.Error -> throw it.error
                is TriState.Progress -> Unit
            }
        }
}

/**
 * Создаёт [SharedFlow], предназначенный для расшаривания стейта.
 *
 * - Если больше нет подписчиков, то отписка от источника происходит сразу.
 * - Ошибка в источнике вызовет передачу ошибки всем подписчикам.
 * - В отличие от [StateFlow] не проверяет одинаковость элементов с помощью equals.
 */
private fun <T> Flow<T>.conflatedSharePass(
    coroutineScope: CoroutineScope,
    stopTimeoutMillis: Long = 0
): Flow<T> {
    return this
        .wrapWithTriState()
        .conflate()
        .shareIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = stopTimeoutMillis,
                replayExpirationMillis = 0
            ),
            replay = 1
        )
        .unwrapTriState()
}

/**
 * Создаёт [SharedFlow], предназначенный для расшаривания стейта.
 *
 * - Если больше нет подписчиков, то отписка от источника происходит с задержкой 1 сек.
 * - Ошибка в источнике вызовет завершение [coroutineScope] с ошибкой.
 * - В отличие от [StateFlow] не проверяет одинаковость элементов с помощью equals.
 */
@Deprecated(
    "Use conflatedShare",
    ReplaceWith(
        "this.conflatedShare(coroutineScope, passErrors = false, distinct = false)"
    )
)
fun <T> Flow<T>.conflatedShare2(
    coroutineScope: CoroutineScope,
    stopTimeoutMillis: Long = 1000
): SharedFlow<T> {
    return this
        .conflate()
        .shareIn(
            coroutineScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = stopTimeoutMillis,
                replayExpirationMillis = 0
            ),
            replay = 1
        )
}

private val NO_VALUE = Any()

/**
 * Работает как [stateIn], но не требует начального значения. Вместо этого ждёт появления
 * значения на источнике.
 *
 * - Если больше нет подписчиков, то отписка от источника происходит с задержкой 1 сек.
 * - Ошибка в источнике вызовет завершение [coroutineScope] с ошибкой.
 */
@Deprecated(
    "Use conflatedShare",
    ReplaceWith(
        "this.conflatedShare(coroutineScope, passErrors = false, distinct = true)",
    )
)
fun <T> Flow<T>.conflatedShare3(
    coroutineScope: CoroutineScope,
    stopTimeoutMillis: Long = 1000
): Flow<T> {
    return stateIn(
        coroutineScope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = stopTimeoutMillis,
            replayExpirationMillis = 0
        ),
        initialValue = NO_VALUE
    ).transform {
        if (it != NO_VALUE) {
            @Suppress("UNCHECKED_CAST")
            emit(it as T)
        }
    }
}

fun Flow<*>.counter(): Flow<Int> = flow {
    var n = 0
    emit(0)

    collect {
        emit(++n)
    }
}

fun <T> Flow<T>.valve(isOpenFlow: Flow<Boolean>): Flow<T> {
    return isOpenFlow.combineTransform(this) { isOpen, value ->
        if (isOpen) {
            emit(value)
        }
    }
}

fun <T> Flow<T>.catchException(action: suspend FlowCollector<T>.(cause: Exception) -> Unit): Flow<T> =
    catch {
        if (it is Exception) {
            action(it)
        } else {
            throw it
        }
    }

fun <T : Any> (suspend () -> T).memoizeSuccess(): Flow<T> {
    val initial = Any()
    val channel = Channel<Any>(capacity = 1)
    channel.trySend(initial)

    return flow {
        val element = channel.receive()
        if (element == initial) {
            val result = invoke()
            emit(result)
            channel.trySend(result)
        } else {
            @Suppress("UNCHECKED_CAST")
            emit(element as T)
        }
    }
}

/**
 * Турникет. Эта функция аналогична `data.zip(turns)`, но забирает
 * данные из `data` только тогда, когда это необходимо (более лениво).
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T : Any> Flow<T>.turnstile(turns: Flow<*>): Flow<T> = flow {
    coroutineScope {
        val elementsChannel: ReceiveChannel<Any> = produce(capacity = Channel.RENDEZVOUS) {
            send(Unit)
            collect {
                send(it)
                send(Unit)
            }
        }
        val turnsChannel: ReceiveChannel<Unit> = produce(capacity = Channel.RENDEZVOUS) {
            turns.collect {
                send(Unit)
            }
        }

        (elementsChannel as SendChannel<*>).invokeOnClose {
            turnsChannel.cancel()
        }

        try {
            turnsChannel.consumeEach {
                elementsChannel.receive()
                @Suppress("UNCHECKED_CAST")
                emit(elementsChannel.receive() as T)
            }
        } finally {
            elementsChannel.cancel()
        }
    }
}

/**
 * Аналогична функции [kotlinx.coroutines.flow.map], но запоминает
 * результат вызова [transform] для аргумента [T].
 */
fun <T, R> Flow<T>.mapCached(transform: suspend (T) -> R): Flow<R> = flow {
    val map = HashMap<T, R>()

    collect {
        val value = map.getOrPut(it) {
            transform(it)
        }
        emit(value)
    }
}

/**
 * Почти тоже самое, что и [kotlinx.coroutines.flow.mapLatest].
 * Отменяет [body] если если в поток приходит новое значение.
 * В стандартной библитеке такой нет, хотя должна быть.
 *
 * TODO: удалить когда появится.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.onEachLatest(body: suspend (T) -> Unit): Flow<T> {
    @Suppress("RemoveExplicitTypeArguments") // Не убирать, иначе будет крашиться хз почему.
    return transformLatest<T, T> {
        body(it)
        emit(it)
    }
}

/**
 * Превращает Flow из TriState в обычный Flow так.
 */
fun <T> Flow<TriState<T>>.unwrapTriState(): Flow<T> {
    return transform {
        when (it) {
            is TriState.Success -> emit(it.value)
            is TriState.Error -> throw it.error
            is TriState.Progress -> Unit
        }
    }
}

/**
 * Похожа на [toTriStateFlow], но всегда завершается
 * значением [TriState.Error] с ошибкой или [CancellationException].
 */
fun <T> Flow<T>.wrapWithTriState(): Flow<TriState<T>> {
    return flow {
        emit(TriState.Progress())

        var finishedError: Exception? = null

        try {
            collect {
                emit(TriState.Success(it))
            }
        } catch (e: Exception) {
            finishedError = e
        }

        emit(TriState.Error<T>(finishedError ?: CancellationException()))
    }
}

fun SharedPreferences.asFlow(): Flow<SharedPreferences> = callbackFlow {
    val listener = SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, _ ->
        this.trySend(sharedPreferences)
    }

    this.trySend(this@asFlow)
    registerOnSharedPreferenceChangeListener(listener)
    awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
}.conflate()

/**
 * Не эмитит значения из искомого flow до тех пор, пока второй [flow] не заэмитит какое-нибудь значение, кроме null.
 *
 * См. [RxMarble.skipUntil](https://rxmarbles.com/#skipUntil)
 */
fun <T1, T2> Flow<T1>.skipUntil(flow: Flow<T2>): Flow<T1> {
    var sendDownstream = false

    return this.combineTransform(flow) { t1, t2 ->
        if (sendDownstream) emit(t1)

        if (!sendDownstream && t2 != null) {
            sendDownstream = true
        }
    }
}

/**
 * Тоже самое, что и [retryWhen], но `attempt` в [predicate] сбрасывается в `0` при успешной попытке.
 */
fun <T> Flow<T>.retryWhenWithAttemptCorrected(predicate: suspend FlowCollector<T>.(cause: Throwable, attempt: Long) -> Boolean): Flow<T> {
    var latestAttempt: Long = -1
    var correction: Long = 0
    return retryWhen { cause, attempt ->
        latestAttempt = attempt
        predicate(cause, attempt - correction)
    }.onEach {
        correction = latestAttempt + 1
    }
}

/**
 * Будет перезапускать цепочку бесконечное количество раз в случае если цепочка завершилась ошибкой.
 *
 * Между перезапусками будет вставлена задержка с увеличивающимся интервалом.
 *
 * @see [delayByAttempt]
 */
fun <T> Flow<T>.retryInfiniteWithPause(): Flow<T> {
    return retryWhenWithAttemptCorrected { cause, attempt ->
        if (cause is Exception) {
            delayByAttempt(attempt.toInt())
            true
        } else {
            false
        }
    }
}

fun <T> Flow<T>.retryInfiniteWithPause(logLambda: suspend (Exception) -> Unit): Flow<T> {
    return retryWhenWithAttemptCorrected { cause, attempt ->
        if (cause is Exception) {
            logLambda.invoke(cause)
            delayByAttempt(attempt.toInt())
            true
        } else {
            false
        }
    }
}

/**
 * Будет перезапускать цепочку X раз в случае если цепочка завершилась ошибкой.
 *
 * Между перезапусками будет вставлена задержка с увеличивающимся интервалом.
 *
 * @see [delayByAttempt]
 */
fun <T> Flow<T>.retryWithPause(times: Int): Flow<T> {
    return retryWhenWithAttemptCorrected { cause, attempt ->
        if (cause is Exception && attempt in 0..times) {
            delayByAttempt(attempt.toInt())
            true
        } else {
            false
        }
    }
}

/**
 * В случае если [body] бросит испключение, повторит [body] не более
 * [times] раз, а затем бросит последнее пойманное исключение.
 *
 * @param delayDurationProvider Возвращает длительность задержки между повторами, по-умолчанию [delayByAttempt]
 * @param exceptionHandler Внешний обработчик для исключений кроме CancellationException, по-умолчанию отсутствует
 *
 * @see [delayByAttempt].
 */
suspend fun <R> retryIfError(
    times: Int,
    delayDurationProvider: ((attempt: Int) -> Duration)? = null,
    exceptionHandler: ((e: Exception) -> Unit)? = null,
    body: suspend () -> R,
): R {
    require(times >= 0)
    var lastError: Exception? = null

    // Тут нет ошибки. Диапазон может быть например таким: [0, 0] - одна итерация, [0, 1] - две итрации и т.д.
    for (attempt in 0..times) {
        try {
            if (attempt > 0) {
                if (delayDurationProvider == null) {
                    delayByAttempt(attempt)
                } else {
                    delay(delayDurationProvider(attempt))
                }
            }

            return body()
        } catch (e: CancellationException) {
            // Отмена есть отмена. Не повторяем.
            throw e
        } catch (e: Exception) {
            exceptionHandler?.invoke(e)
            lastError = e
        }
    }

    throw lastError!!
}

/**
 * Минимальная задержка: 2.5 сек
 * Максимальная задержка: 240 сек
 */
suspend fun delayByAttempt(attempt: Int) {
    delay(1.5.pow(attempt.toDouble()).plus(1.5).coerceAtMost(240.0).seconds)
}

suspend fun delayWithQuadraticBackoff(
    attempt: Int,
    maximumValue: Int = 240,
) {
    delay(
        duration = 2.toDouble().pow(attempt).seconds.coerceAtMost(maximumValue.seconds)
    )
}

/**
 * Позволяет разорвать Operator Fusion, что полезно при тонком управлении цепочками с буфером.
 */
fun <T> Flow<T>.breakFusion(): Flow<T> = flow { emitAll(this@breakFusion) }

fun <T> Flow<T>.collectIn(scope: CoroutineScope, collector: suspend (T) -> Unit): Job {
    return onEach(collector).launchIn(scope)
}

@Deprecated(
    message = "This method swallowed TimeoutCancellationException, and can break cancellation in some cases",
    replaceWith = ReplaceWith(
        expression = "this.rethrowCancellation()",
    ),
    level = DeprecationLevel.WARNING,
)
fun Exception.rethrowIfCancellation() {
    if (this is CancellationException) {
        throw this
    }
}

fun <T> flowOf(getValue: suspend () -> T): Flow<T> =
    flow {
        emit(getValue())
    }
