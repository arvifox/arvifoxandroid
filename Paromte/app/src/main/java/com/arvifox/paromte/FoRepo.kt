package com.arvifox.paromte

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlin.math.ceil
import kotlin.math.floor

@ExperimentalPagingApi
class FooMeda() : RemoteMediator<Int, FoEntity>() {

    val dudu = daa!!

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, FoEntity>
    ): MediatorResult {
        dod("load $loadType ${state.lastItemOrNull()?.name}")
        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val ls = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                    state.pages.size + 1
                }
            }
            var ca = ceil(dudu.fodao().couro().toFloat() / state.config.pageSize).toLong() + 1
            if (loadType == LoadType.REFRESH) ca = 1
            dod("ca = $ca")
            val resp = api.getTransactionsPaged(
                "",
                ca,
                if (loadType == LoadType.REFRESH) state.config.initialLoadSize else state.config.pageSize
            )
            dod("resp = ${resp.data.size}")
            dudu.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dudu.fodao().clearFo()
                }
                dudu.fodao().insertFo(resp.data.map {
                    FoEntity(it.id)
                })
            }
            dod("indb = ${dudu.fodao().couro()}")
            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Throwable) {
            MediatorResult.Error(e)
        }
    }

    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH
}

@ExperimentalPagingApi
class FoRepo() {

    private val meda = FooMeda()

    private val pager by lazy {
        Pager(
            config = PagingConfig(
                initialLoadSize = 40,
                pageSize = 20,
                prefetchDistance = 10
            ), remoteMediator = meda, pagingSourceFactory = { daa!!.fodao().paging() }
        )
    }

    fun getflow(): Flow<PagingData<FoEntity>> = pager.flow

}