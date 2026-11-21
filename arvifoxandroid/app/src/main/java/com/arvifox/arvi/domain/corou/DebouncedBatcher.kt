package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Утилита для накопления событий и их пакетной обработки (Debounce).
 * @param scope CoroutineScope, в котором будет работать таймер.
 * @param delayMs Время ожидания перед отправкой пачки.
 * @param onFlush Лямбда, которая будет вызвана с накопленным списком.
 */
class DebouncedBatcher<T>(
    private val scope: CoroutineScope,
    private val delayMs: Long,
    private val onFlush: (List<T>) -> Unit,
) {
    private val channel = Channel<T>(Channel.UNLIMITED)

    init {
        scope.launch {
            for (firstItem in channel) {
                val batch = ArrayList<T>()
                batch.add(firstItem)

                delay(delayMs)

                var result = channel.tryReceive()
                while (result.isSuccess) {
                    batch.add(result.getOrThrow())
                    result = channel.tryReceive()
                }

                try {
                    onFlush(batch)
                } catch (e: Exception) {
                    println("Ошибка flushing batch")
                }
            }
        }
    }

    fun submit(item: T) {
        channel.trySend(item)
    }

    fun close() {
        channel.close()
    }

    companion object {

        private const val DEBOUNCED_BATCHER_TAG = "DebouncedBatcher"
    }
}