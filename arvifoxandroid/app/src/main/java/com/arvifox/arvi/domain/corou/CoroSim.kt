package com.arvifox.arvi.domain.corou

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.time.Duration
import kotlin.time.measureTime

object CoroSim {
    private val ms = MainScope()

    private fun fooca(f: FooCa): FooCa {
        return f.copy(d = f.d.inc(), gh = f.gh + "_cad")
    }

    private data class FooCa(
        val d: Int,
        val gh: String,
    )

    private val foos =
        listOf(
            FooCa(
                d = 23,
                gh = "gh1",
            ),
            FooCa(
                d = 34,
                gh = "gh2",
            ),
            FooCa(
                d = 45,
                gh = "gh3",
            ),
            FooCa(
                d = 56,
                gh = "gh4",
            ),
            FooCa(
                d = 67,
                gh = "gh5",
            ),
        )

    suspend fun calcall() {
        val ese: ExecutorService = Executors.newFixedThreadPool(foos.size)
        val coro: Duration = calcCoro()
        val flow: Duration = calcFlow()
        val eses: Duration = calcEse(ese)
        println("coro = ${coro.inWholeMicroseconds}")
        println("flow = ${flow.inWholeMicroseconds}")
        println("eses = ${eses.inWholeMicroseconds}")
        ese.shutdown()
    }

    suspend fun calcCoro() =
        measureTime {
            val all: List<Deferred<FooCa>> =
                foos.map { f ->
                    ms.async {
                        fooca(f)
                    }
                }
            val alld: List<FooCa> = all.awaitAll()
            println("calcCoroalld ${alld.size}")
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun calcFlow() =
        measureTime {
            val all: Flow<FooCa> =
                foos.asFlow()
                    .flatMapMerge {
                        flow {
                            emit(fooca(it))
                        }
                    }
            val alld: List<FooCa> = all.toList()
            println("calcFlowalld ${alld.size}")
        }

    fun calcEse(service: ExecutorService) =
        measureTime {
            val all: List<Future<FooCa>> =
                foos.map {
                    service.submit(Callable { fooca(it) })
                }
            val alld: List<FooCa> = all.map { it.get() }
            println("calcEsealld ${alld.size}")
        }
}
