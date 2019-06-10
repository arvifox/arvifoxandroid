package com.arvifox.arvi.simplemisc.viewpager2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.arvifox.arvi.R
import com.arvifox.arvi.simplemisc.viewpager2.tabviewpager.TabViewPagerAdapter
import kotlinx.android.synthetic.main.activity_view_pager2.*
import kotlinx.android.synthetic.main.app_bar_layout.*

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

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            super.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }

        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pager2)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        vpViewpager.adapter = TabViewPagerAdapter(supportFragmentManager)
        tlViewpager.setupWithViewPager(vpViewpager, true)

//        vpViewpager2.adapter = VpAdapter()
//        vpViewpager2.orientation = ViewPager2.ORIENTATION_VERTICAL

        vpViewpager2.adapter = ViewPagerFragmentStateAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        vpViewpager2.registerOnPageChangeCallback(pageCallback)
    }

    override fun onPause() {
        vpViewpager2.unregisterOnPageChangeCallback(pageCallback)
        super.onPause()
    }
}
