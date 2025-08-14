package com.arvifox.arvi.uicompose.anilikes

import android.animation.ValueAnimator
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.delay
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
        val ofy: MutableFloatState = mutableFloatStateOf(0.0f),
    ) {
        fun setAni(va: ValueAnimator) {
            va.addUpdateListener {
                ofy.floatValue = it.animatedValue as Float
            }
            va.start()
        }
    }

    private var currentId = 0
    private var yOff: Float = 0.0f

    fun setUpAni(yOf: Float) {
        yOff = yOf
    }

    fun onFinish(id: IconState) {
        _iconsState.update { ui ->
            ui.copy(
                likes = ui.likes.remove(id)
            )
        }
    }

    fun showIcon() {
        viewModelScope.launch {
            val vaan = ValueAnimator.ofFloat(0.0f, yOff).apply {
                duration = 2000

            }
            val newIcon = IconState(id = currentId++)
            newIcon.setAni(vaan)
            _iconsState.update { ui ->
                ui.copy(
                    likes = ui.likes.add(newIcon)
                )
            }
            launch {
                delay(2000)
                onFinish(newIcon)
            }
        }
    }
}