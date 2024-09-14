package com.arvifox.fulltestcompose.icons

import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.group

val myIcon: ImageVector = materialIcon("") {
    materialPath(fillAlpha = 0.8f) {
        moveTo(20f, 10f)
        lineToRelative(0f, 10f)
        lineToRelative(-10f, 0f)
        close()
    }
    group {
        materialPath(pathFillType = PathFillType.EvenOdd) {
            moveTo(0f, 10f)
            lineToRelative(-10f, 0f)
            close()
        }
    }
}