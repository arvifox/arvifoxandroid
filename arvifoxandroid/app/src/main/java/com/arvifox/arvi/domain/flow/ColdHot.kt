package com.arvifox.arvi.domain.flow

import android.location.Location
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.Eagerly
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn

class LocationRepository(
    private val externalScope: CoroutineScope
) {
    fun Flow<Location>.shareInScope() = this.shareIn(externalScope, WhileSubscribed())

    fun Flow<Location>.shareLast10() = this.shareIn(externalScope, Eagerly, 10)

    fun Flow<Location>.stateInLast() = this.stateIn(externalScope, WhileSubscribed(), Location(""))
}