package com.arvifox.arvi.uicompose.draw

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.abs

@Composable
fun DrawMainScree(modifier: Modifier = Modifier) {
    val vm = viewModel<DrawViewModel>()
    val sss = vm.state.collectAsStateWithLifecycle()
    Column(
        modifier = modifier.fillMaxSize().background(color = Color.DarkGray),
    ) {
        Dra(
            modifier = Modifier.fillMaxWidth().weight(1f),
            paths = sss.value.paths,
            curPath = sss.value.curPath,
            onAction = vm::onAction,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            allColors.fastForEach {
                val isSelected = sss.value.curColor == it
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            val sc = if (isSelected) 1.2f else 1f
                            scaleY = sc
                            scaleX = sc
                        }
                        .size(30.dp)
                        .clip(CircleShape)
                        .background(it)
                        .border(
                            width = 2.dp,
                            color = if (isSelected) Color.Black else Color.LightGray,
                            shape = CircleShape,
                        )
                        .clickable(onClick = { vm.onAction(DrawingAction.OnSelectColor(it)) }),
                ) {

                }
            }
        }
        Button(onClick = { vm.onAction(DrawingAction.OnClearCanvas) }) {
            Text("clear")
        }
    }
}

@Composable
private fun Dra(
    modifier: Modifier,
    paths: List<PathInfo>,
    curPath: PathInfo?,
    onAction: (DrawingAction) -> Unit,
) {
    Canvas(
        modifier = modifier
            .clipToBounds()
            .background(color = Color.White)
            .pointerInput(true) {
                detectDragGestures(
                    onDragStart = { onAction(DrawingAction.OnPathStart) },
                    onDragEnd = { onAction(DrawingAction.OnPathEnd) },
                    onDrag = { pointerInputChange, amount ->
                        onAction(DrawingAction.OnDraw(pointerInputChange.position))
                    },
                    onDragCancel = { onAction(DrawingAction.OnPathEnd) },
                )
            },
    ) {
        paths.fastForEach { pathInfo ->
            draPath(
                path = pathInfo.path,
                color = pathInfo.color,
            )
        }
        curPath?.let {
            draPath(it.path, it.color)
        }
    }
}

private fun DrawScope.draPath(
    path: List<Offset>,
    color: Color,
    thickness: Float = 10f,
) {
    check(path.isNotEmpty())
    val sp = Path().apply {
        moveTo(path.first().x, path.first().y)
        val smoothness = 5
        for (i in 1..path.lastIndex) {
            val from = path[i - 1]
            val to = path[i]
            val dx = abs(to.x - from.x)
            val dy = abs(to.y - from.y)
            if (dx >= smoothness || dy >= smoothness) {
                quadraticTo(
                    x1 = (from.x + to.x) / 2f,
                    y1 = (from.y + to.y) / 2f,
                    x2 = to.x,
                    y2 = to.y,
                )
            }
        }
    }
    drawPath(
        path = sp,
        color = color,
        style = Stroke(
            width = thickness,
            cap = StrokeCap.Round,
            join = StrokeJoin.Round,
        ),
    )
}