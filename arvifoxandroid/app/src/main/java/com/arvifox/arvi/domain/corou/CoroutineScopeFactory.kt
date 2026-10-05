package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

object CoroutineExceptionHandlerHolder {

    /**
     * Поле может быть null только в тестах, где этот хендлер не нужен.
     * Во всех остальных случаях необходимо его проинициализировать
     */
    var uncaughtExceptionHandler: CoroutineExceptionHandler? = null
}

class RootCoroutineScope(job: Job, coroutineName: String, dispatcher: CoroutineDispatcher) : CoroutineScope {

    constructor(name: String, dispatcher: CoroutineDispatcher) : this(
        job = SupervisorJob(),
        coroutineName = name,
        dispatcher = dispatcher,
    )

    override val coroutineContext: CoroutineContext = CoroutineExceptionHandlerHolder.uncaughtExceptionHandler?.let { handler ->
        dispatcher + job + CoroutineName(coroutineName) + handler
    } ?: (dispatcher + job + CoroutineName(coroutineName))
}

interface CoroutineScopeFactory {

    fun createUIScope(name: String): CoroutineScope

    fun createBackgroundScope(name: String): CoroutineScope
}

@Suppress("FunctionName", "unused")
inline fun <reified T> T.BackgroundCoroutineScope(factory: CoroutineScopeFactory) =
    factory.createBackgroundScope(name = T::class.java.simpleName)

@Suppress("FunctionName", "unused")
inline fun <reified T> T.UICoroutineScope(factory: CoroutineScopeFactory) =
    factory.createUIScope(name = T::class.java.simpleName)

/**
 * `CoroutineScope`-ы, созданные этой фабрикой живут всё время жизни приложения.
 */
class AppCoroutineScopeFactoryImpl(
    private val immediate: CoroutineDispatcher,
    private val default: CoroutineDispatcher,
) : CoroutineScopeFactory {

    private val job = SupervisorJob()

    override fun createUIScope(name: String): CoroutineScope = RootCoroutineScope(
        job = SupervisorJob(job),
        coroutineName = name,
        dispatcher = immediate
    )

    override fun createBackgroundScope(name: String): CoroutineScope = RootCoroutineScope(
        job = SupervisorJob(job),
        coroutineName = name,
        dispatcher = default
    )
}