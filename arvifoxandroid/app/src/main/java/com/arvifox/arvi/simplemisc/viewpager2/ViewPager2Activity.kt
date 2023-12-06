package com.arvifox.arvi.simplemisc.viewpager2

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import androidx.annotation.DimenRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.arvifox.arvi.R
import com.arvifox.arvi.databinding.ActivityViewPager2Binding
import com.arvifox.arvi.simplemisc.viewpager2.tabviewpager.TabViewPagerAdapter
import kotlin.math.abs
import kotlin.math.max

/*
https://proandroiddev.com/look-deep-into-viewpager2-13eb8e06e419

https://stackoverflow.com/questions/10098040/android-viewpager-show-preview-of-page-on-left-and-right
 */

class ViewPager2Activity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, ViewPager2Activity::class.java)
        }
    }

    val pageCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrollStateChanged(state: Int) {
            super.onPageScrollStateChanged(state)
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    private lateinit var binding: ActivityViewPager2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPager2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incAppBar.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.vpViewpager.adapter = TabViewPagerAdapter(supportFragmentManager)
        binding.vpViewpager.pageMargin = 30
        binding.tlViewpager.setupWithViewPager(binding.vpViewpager, true)

        binding.vpViewpager2.adapter = VpAdapter()
        binding.vpViewpager2.orientation = ViewPager2.ORIENTATION_VERTICAL
        binding.vpViewpager2.clipChildren = false
        binding.vpViewpager2.clipToPadding = false
        binding.vpViewpager2.offscreenPageLimit = 3

        val pageMargin = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offset = resources.getDimensionPixelOffset(R.dimen.offset)
        binding.vpViewpager2.setPageTransformer { page, position ->
            val vp = page.parent.parent as ViewPager2
            val of = position * -(2 * offset + pageMargin)
            if (vp.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(vp) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -of
                } else {
                    page.translationX = of
                }
            } else {
                page.translationY = of
            }
        }
//        vpViewpager2.adapter = ViewPagerFragmentStateAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        binding.vpViewpager2.registerOnPageChangeCallback(pageCallback)
    }

    override fun onPause() {
        binding.vpViewpager2.unregisterOnPageChangeCallback(pageCallback)
        super.onPause()
    }
}

class ViewPager2PageTransformation : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        val absPos = abs(position)
        page.apply {
            translationY = absPos * 500f
            translationX = absPos * 500f
            scaleX = 1f
            scaleY = 1f
        }
        when {
            position < -1 ->
                page.alpha = 0.1f
            position <= 1 -> {
                page.alpha = max(0.2f, 1 - abs(position))
            }
            else -> page.alpha = 0.1f
        }
    }
}

class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
    RecyclerView.ItemDecoration() {

    private val horizontalMarginInPx: Int =
        context.resources.getDimension(horizontalMarginInDp).toInt()

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.right = horizontalMarginInPx
        outRect.left = horizontalMarginInPx
    }
}
