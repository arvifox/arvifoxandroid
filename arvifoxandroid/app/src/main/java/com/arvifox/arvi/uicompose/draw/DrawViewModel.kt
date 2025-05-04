package com.arvifox.arvi.uicompose.draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DrawingState(
    val curColor: Color = Color.Blue,
    val curPath: PathInfo? = null,
    val paths: List<PathInfo> = emptyList(),
)

val allColors = listOf(Color.Blue, Color.Black, Color.Red, Color.Green, Color.Magenta, Color.Gray)

data class PathInfo(
    val id: String,
    val color: Color,
    val path: List<Offset>,
)

sealed interface DrawingAction {
    data object OnClearCanvas : DrawingAction
    data object OnPathStart : DrawingAction
    data object OnPathEnd : DrawingAction
    data class OnSelectColor(val color: Color) : DrawingAction
    data class OnDraw(val o: Offset) : DrawingAction
}

class DrawViewModel : ViewModel() {

    private val _state = MutableStateFlow(DrawingState())
    val state = _state.asStateFlow()

    fun onAction(action: DrawingAction) {
        when (action) {
            DrawingAction.OnClearCanvas -> {
                _state.update {
                    it.copy(curPath = null, paths = emptyList())
                }
            }

            is DrawingAction.OnDraw -> {
                val cp = state.value.curPath ?: return
                _state.update {
                    it.copy(
                        curPath = cp.copy(path = cp.path + action.o)
                    )
                }
            }

            DrawingAction.OnPathEnd -> {
                val cp = state.value.curPath ?: return
                _state.update {
                    it.copy(
                        curPath = null,
                        paths = it.paths + cp,
                    )
                }
            }

            DrawingAction.OnPathStart -> {
                _state.update {
                    it.copy(
                        curPath = PathInfo(
                            id = System.currentTimeMillis().toString(),
                            color = it.curColor,
                            path = emptyList(),
                        )
                    )
                }
            }

            is DrawingAction.OnSelectColor -> {
                _state.update {
                    it.copy(curColor = action.color)
                }
            }
        }
    }

}