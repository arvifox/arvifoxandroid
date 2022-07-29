package com.arvifox.arvi.widget

import android.content.Context
import android.content.res.Resources
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.ContextThemeWrapper
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.arvifox.arvi.R
import com.google.android.material.theme.MaterialComponentsViewInflater

/**
 * [https://proandroiddev.com/assign-multiple-styles-to-a-single-view-in-android-just-like-in-css-815040be4c2b]
 */
fun instantiate(context: Context, attrs: AttributeSet) {
    val someTextView = TextView(ContextThemeWrapper(context, R.style.CardView_Dark))

    val styleAttributes = context.obtainStyledAttributes(attrs, R.styleable.MuTextView)
    val attribute = styleAttributes.getResourceId(R.styleable.MuTextView_style1, 0)
    val theme = context.resources.newTheme()
    // be mindful that the second parameter should be true!
    theme.applyStyle(attribute, true)
    val ctw = ContextThemeWrapper(context, theme)
}

/**
Creates and replaces the context of a view with the combined styles added to the view if it has any.
 */
class MultiStyleViewInflater : MaterialComponentsViewInflater() {
    // override the creators of any view you want to have multiple styles
    override fun createTextView(context: Context, attrs: AttributeSet?): AppCompatTextView {
        // create context if needed and set the attributes as usual
        return super.createTextView(createContextIfMultiStyle(context, attrs), attrs)
    }

    // override fun anyOtherView as needed ...

    private fun createContextIfMultiStyle(context: Context, attrs: AttributeSet?): Context {
        // get our handy custom attributes
        val styleAttributes = context.obtainStyledAttributes(attrs, R.styleable.MuTextView)

        // collect the styles added to the view
        val styles = extractStyles(styleAttributes)

        // create the custom ContextThemeWrapper only if the view has a custom multi style attribute
        val createdContext = if (styles.any { it != 0 }) {
            // create a theme, add styles and create the wrapper using the theme
            val theme = context.resources.newTheme()

            theme.applyValidStyles(styles)
            ContextThemeWrapper(context, theme)
        } else {
            // or just return the original context
            context
        }

        // don't forget to call this!
        styleAttributes.recycle()

        return createdContext
    }

    private fun extractStyles(styleAttributes: TypedArray) = listOf(
        // the zero values help us determine if we have a custom style added at all
        styleAttributes.getResourceId(R.styleable.MuTextView_style1, 0),
        styleAttributes.getResourceId(R.styleable.MuTextView_style2, 0),
        styleAttributes.getResourceId(R.styleable.MuTextView_style3, 0),
        styleAttributes.getResourceId(R.styleable.MuTextView_style4, 0),
        styleAttributes.getResourceId(R.styleable.MuTextView_style5, 0)
    )

    private fun Resources.Theme.applyValidStyles(styles: List<Int>) {
        // adding styles that actually exist. note we force update duplicate attributes
        styles.filterNot { it == 0 }.forEach { this.applyStyle(it, true) }
    }
}
