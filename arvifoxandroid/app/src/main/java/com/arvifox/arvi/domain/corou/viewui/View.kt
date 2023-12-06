package com.arvifox.arvi.domain.corou.viewui

import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

// https://medium.com/androiddevelopers/suspending-over-views-19de9ebd7020
// https://developer.android.com/topic/libraries/architecture/coroutines

object ViewUi {
    suspend fun View.awaitNextLayout() = suspendCancellableCoroutine<Unit> { cont ->
        // This lambda is invoked immediately, allowing us to create
        // a callback/listener
        val listener = object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                view: View?,
                p1: Int,
                p2: Int,
                p3: Int,
                p4: Int,
                p5: Int,
                p6: Int,
                p7: Int,
                p8: Int
            ) {
                // The next layout has happened!
                // First remove the listener to not leak the coroutine
                view?.removeOnLayoutChangeListener(this)
                // Finally resume the continuation, and
                // wake the coroutine up
                cont.resume(Unit)
            }
        }
        // If the coroutine is cancelled, remove the listener
        cont.invokeOnCancellation { removeOnLayoutChangeListener(listener) }
        // And finally add the listener to view
        addOnLayoutChangeListener(listener)
        // The coroutine will now be suspended. It will only be resumed
        // when calling cont.resume() in the listener above
    }

    fun howtouse(titleView: TextView) {
        // viewLifecycleOwner.lifecycleScope.launch {
        runBlocking {
            // Make the view invisible and set some new text
            titleView.isInvisible = true
            titleView.text = "Hi everyone!"

            // Wait for the next layout pass to know
            // the height of the view
            titleView.awaitNextLayout()

            // Layout has happened!
            // We can now make the view visible, translate it up, and then animate it
            // back down
            titleView.isVisible = true
            titleView.translationY = -titleView.height.toFloat()
            titleView.animate().translationY(0f)
        }
    }

    fun dfkj(fr: Fragment) {
        fr.viewLifecycleOwner.lifecycleScope.launch {

        }
    }
}