package com.arvifox.arvi.domain.corou

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

enum class AppState {
    Foreground, Background
}

enum class AppEvent {
    ON_CREATE, ON_START, ON_STOP, ON_DESTROY
}

interface ApplicationVisibilitySource {

    fun observe(): Flow<AppState>

    fun observeEvent(): Flow<AppEvent>

    fun observeIsForeground(): Flow<Boolean>

    suspend fun awaitForeground()
}

@Singleton
internal class ApplicationVisibilitySourceImpl @Inject constructor(
    coroutineScopeFactory: CoroutineScopeFactory,
) : ApplicationVisibilitySource {

    /**
     * Реализация подписки в addObserver не потокобезопасная,
     * поэтому работаем с ней только на UI треде.
     */
    private val sourceScope = UICoroutineScope(coroutineScopeFactory)

    private val appEventFlow = MutableStateFlow(AppEvent.ON_CREATE)

    private val state = callbackFlow {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    this.trySend(AppState.Foreground)
                    appEventFlow.value = AppEvent.ON_START
                }

                Lifecycle.Event.ON_STOP -> {
                    this.trySend(AppState.Background)
                    appEventFlow.value = AppEvent.ON_STOP
                }

                Lifecycle.Event.ON_CREATE -> {
                    appEventFlow.value = AppEvent.ON_CREATE
                }

                Lifecycle.Event.ON_DESTROY -> {
                    appEventFlow.value = AppEvent.ON_DESTROY
                }

                else -> {}
            }
        }

        val lifecycle = ProcessLifecycleOwner.get().lifecycle
        lifecycle.addObserver(observer)

        awaitClose {
            lifecycle.removeObserver(observer)
        }
    }.stateIn(
        scope = sourceScope,
        started = SharingStarted.WhileSubscribed(
            stopTimeoutMillis = 1000,
            replayExpirationMillis = 0
        ),
        initialValue = AppState.Background
    )

    override fun observe(): Flow<AppState> = state

    override fun observeEvent(): Flow<AppEvent> = appEventFlow.asStateFlow()

    override fun observeIsForeground(): Flow<Boolean> =
        observe().map { it == AppState.Foreground }.distinctUntilChanged()

    override suspend fun awaitForeground() {
        observe().first { it == AppState.Foreground }
    }
}