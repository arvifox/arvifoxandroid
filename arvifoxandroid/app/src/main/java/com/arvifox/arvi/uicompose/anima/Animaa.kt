import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A custom shape that creates a clock wipe effect based on the given progress.
 *
 * @param progress The current progress of the wipe animation (0f to 1f).
 * @param startAngle The angle at which the wipe starts, default is 12 o'clock (-90 degrees).
 * @param isClockwise Determines if the wipe should proceed in a clockwise direction.
 */
class ClockWipeShape(
    private val progress: Float,
    private val startAngle: Float = -90f,
    private val isClockwise: Boolean = true
) : Shape {

    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val targetAngle = if (isClockwise) -360f else 360f
        val arcFraction = 1f - progress.coerceIn(0f, 1f)
        val sweepAngle = targetAngle * arcFraction

        val diagonal = sqrt(size.width.pow(2) + size.height.pow(2))
        val arcRect = Rect(size.center, diagonal / 2)

        val path = Path().apply {
            if (arcFraction != 1f) {
                arcTo(arcRect, startAngle, sweepAngle, true)
                lineTo(size.center.x, size.center.y)
                close()
            } else {
                addRect(size.toRect())
            }
        }

        return Outline.Generic(path)
    }

}

/**
 * A Modifier extension that applies a clock wipe animation to a composable.
 *
 * @param isVisible Controls the visibility of the content (`true` = content is visible, no effect applied).
 * @param startAngle The starting angle of the wipe, default is 12 o'clock (-90 degrees).
 * @param isClockwise Whether the wipe animation progresses clockwise.
 * @param animationSpec The animation specification, default is a 1000ms tween.
 * @param onFinish An optional callback invoked when the animation finishes.
 * @return A Modifier with the clock wipe animation applied.
 */
@Composable
fun Modifier.clockWipeAnimation(
    isVisible: Boolean,
    startAngle: Float = -90f,
    isClockwise: Boolean = true,
    animationSpec: AnimationSpec<Float> = tween(1000),
    onFinish: ((isVisible: Boolean) -> Unit)? = null
): Modifier {
    val progress by animateFloatAsState(
        targetValue = if (isVisible) 0f else 1f,
        animationSpec = animationSpec,
        label = "ClockWipeProgress",
        finishedListener = { currentProgress ->
            onFinish?.invoke(currentProgress == 0f)
        }
    )

    return this.clockWipe(progress, startAngle, isClockwise)
}

/**
 * A Modifier extension that clips a composable using a clock wipe effect.
 *
 * @param progress The current progress of the wipe (0f to 1f).
 * @param startAngle The starting angle of the wipe, default is 12 o'clock (-90 degrees).
 * @param isClockwise Whether the wipe progresses clockwise.
 * @return A Modifier with the clock wipe effect applied.
 */
fun Modifier.clockWipe(
    progress: Float,
    startAngle: Float = -90f,
    isClockwise: Boolean = true
) = this.clip(ClockWipeShape(progress, startAngle, isClockwise))