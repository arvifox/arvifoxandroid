package com.arvifox.arvi.domain.corou.viewui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.reflect.KProperty0

fun View.animateProperty(
    property: KProperty0<Float>,
    fromValue: Float,
    toValue: Float,
    duration: Long,
    onComplete: () -> Unit = {}
) {
    val animator = ObjectAnimator.ofFloat(this, property.name, fromValue, toValue).apply {
        setDuration(duration)
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                onComplete()
            }
        })
    }
    animator.start()
}

object Voew2 {

    suspend fun Animator.awaitEnd() = suspendCancellableCoroutine<Unit> { cont ->
        // Add an invokeOnCancellation listener. If the coroutine is
        // cancelled, cancel the animation too that will notify
        // listener's onAnimationCancel() function
        cont.invokeOnCancellation { cancel() }

        addListener(object : AnimatorListenerAdapter() {
            private var endedSuccessfully = true

            override fun onAnimationCancel(animation: Animator) {
                // Animator has been cancelled, so flip the success flag
                endedSuccessfully = false
            }

            override fun onAnimationEnd(animation: Animator) {
                // Make sure we remove the listener so we don't keep
                // leak the coroutine continuation
                animation.removeListener(this)

                if (cont.isActive) {
                    // If the coroutine is still active...
                    if (endedSuccessfully) {
                        // ...and the Animator ended successfully, resume the coroutine
                        cont.resume(Unit)
                    } else {
                        // ...and the Animator was cancelled, cancel the coroutine too
                        cont.cancel()
                    }
                }
            }
        })
    }

    fun sdf(fr: Fragment, imageView: ImageView) {
        fr.viewLifecycleOwner.lifecycleScope.launch {
            ObjectAnimator.ofFloat(imageView, View.ALPHA, 0f, 1f).run {
                start()
                awaitEnd()
            }

            ObjectAnimator.ofFloat(imageView, View.TRANSLATION_Y, 0f, 100f).run {
                start()
                awaitEnd()
            }

            ObjectAnimator.ofFloat(imageView, View.TRANSLATION_X, -100f, 0f).run {
                start()
                awaitEnd()
            }
        }
    }

}