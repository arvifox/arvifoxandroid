package com.arvifox.arvi.domain.corou

import com.arvifox.arvi.domain.TriState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

const val debug = true

internal class LoadJobs<T> private constructor(
    private val logException: (Exception) -> Unit,
    private val jobOwner: JobOwner,
    private val onState: (TriState<T>) -> Unit,
) {

    constructor(
        onException: (Exception) -> Unit,
        scope: CoroutineScope,
        onState: (TriState<T>) -> Unit,
    ) : this(
        logException = onException,
        jobOwner = JobOwner(scope),
        onState = onState,
    )

    fun cancel() {
        jobOwner.cancel()
    }

    fun onState(onState: (TriState<T>) -> Unit): LoadJobs<T> = LoadJobs(logException, jobOwner, onState)

    fun catch(catch: (e: Exception) -> T): LoadJobs<T> = LoadJobs(logException, jobOwner) { state: TriState<T> ->
        if (state is TriState.Error) {
            try {
                val result = catch(state.error)
                onState(TriState.Success(result))
            } catch (e: Exception) {
                onState(TriState.Error(e))
            }
        } else {
            onState(state)
        }
    }

    fun load(block: suspend CoroutineScope.() -> T): Job {
        onState(TriState.Progress())

        // Используется чтобы дополнить стектрейс местом вызова загрузки.
        val diagnosticError = if (debug) {
            RuntimeException("LoadJobs Failed")
        } else {
            null
        }

        return jobOwner.launch {
            fun onError(e: Exception) {
                val error = diagnosticError?.apply { initCause(e) } ?: e
                logException(error)
                onState(TriState.Error(e))
            }
            try {
                val result = coroutineScope(block)
                onState(TriState.Success(result))
            } catch (e: TimeoutCancellationException) {
                onError(e)
            } catch (_: CancellationException) {
                // ignore
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    /**
     * Загрузка без смены состояния экрана, чтобы избежать дискотеки
     */
    fun loadNoProgressState(block: suspend CoroutineScope.() -> T): Job {
        return jobOwner.launch {
            fun onError(e: Exception) {
                logException(e)
                onState(TriState.Error(e))
            }
            try {
                val result = coroutineScope(block)
                onState(TriState.Success(result))
            } catch (e: TimeoutCancellationException) {
                onError(e)
            } catch (_: CancellationException) {
                // ignore
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    /**
     * Загрузка без установки успешного состояния после выполнения
     */
    fun loadNoSuccessState(block: suspend CoroutineScope.() -> T): Job {
        onState(TriState.Progress())
        return jobOwner.launch {
            fun onError(e: Exception) {
                logException(e)
                onState(TriState.Error(e))
            }
            try {
                coroutineScope(block)
            } catch (e: TimeoutCancellationException) {
                onError(e)
            } catch (_: CancellationException) {
                // ignore
            } catch (e: Exception) {
                onError(e)
            }
        }
    }

    fun isActive(): Boolean {
        return jobOwner.isActive()
    }

    class JobOwner(private val scope: CoroutineScope) {

        private var job: Job? = null

        fun cancel() {
            job?.cancel()
        }

        fun launch(block: suspend CoroutineScope.() -> Unit): Job {
            job?.cancel()
            job = scope.launch(block = block)
            return job!!
        }

        fun isActive(): Boolean {
            return job?.isActive == true
        }
    }
}