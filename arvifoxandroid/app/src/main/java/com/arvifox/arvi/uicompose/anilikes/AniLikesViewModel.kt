package com.arvifox.arvi.uicompose.anilikes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AniLikesViewModel() : ViewModel() {

    private val _iconsState = MutableStateFlow(UiState())
    val iconsState = _iconsState.asStateFlow()

    data class UiState(
        val likes: PersistentList<IconState> = persistentListOf(),
    )

    data class IconState(
        val id: Int,
    )

    private var currentId = 0

    fun onStartAni(id: Int, target: Float) {
    }

    fun onFinish(id: IconState) {
        _iconsState.update { ui ->
            ui.copy(
                likes = ui.likes.toMutableList().let {
                    it.remove(id)
                    it.toPersistentList()
                }
            )
        }
    }

    fun showIcon() {
        viewModelScope.launch {
            val newIcon = IconState(id = currentId++)
            _iconsState.update { ui ->
                ui.copy(
                    likes = ui.likes.toMutableList().let {
                        it.add(newIcon)
                        it.toPersistentList()
                    }
                )
            }
        }
    }
}