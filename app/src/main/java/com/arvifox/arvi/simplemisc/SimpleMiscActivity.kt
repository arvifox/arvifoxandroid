package com.arvifox.arvi.simplemisc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.R
import com.arvifox.arvi.simplemisc.anim.AnimActivity
import com.arvifox.arvi.simplemisc.camera.CameraShotActivity
import com.arvifox.arvi.simplemisc.constrlayout.ConstrLayoutActivity
import com.arvifox.arvi.simplemisc.matcomp.MatCompActivity
import com.arvifox.arvi.simplemisc.matcomp.TaskColorActivity
import com.arvifox.arvi.simplemisc.misc4window.WindowActivity
import com.arvifox.arvi.simplemisc.moveimage.MoveImageActivity
import com.arvifox.arvi.simplemisc.opencv.OpencvActivity
import com.arvifox.arvi.simplemisc.sensor.SensorActivity
import com.arvifox.arvi.simplemisc.servicehandler.ServiceHandlerActivity
import com.arvifox.arvi.simplemisc.tmdb.TmdbActivity
import com.arvifox.arvi.simplemisc.view.NestedScrollActivity
import com.arvifox.arvi.simplemisc.viewpager2.ViewPager2Activity
import com.arvifox.arvi.simplemisc.workmanager.WorkManSelectActivity
import kotlinx.android.synthetic.main.activity_simple_misc.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class SimpleMiscActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, SimpleMiscActivity::class.java)
        }
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_misc)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        btnCameraStart.setOnClickListener { startActivity(CameraShotActivity.newIntent(this)) }
        btnSensor.setOnClickListener { startActivity(SensorActivity.newIntent(this)) }
        btnServiceHandler.setOnClickListener { startActivity(ServiceHandlerActivity.newIntent(this)) }
        btnMatComp.setOnClickListener { startActivity(MatCompActivity.newIntent(this)) }
        btnAnim.setOnClickListener { startActivity(AnimActivity.newIntent(this)) }
        btnWorkManager.setOnClickListener { startActivity(WorkManSelectActivity.newIntent(this)) }
        btnNestedScroll.setOnClickListener { startActivity(NestedScrollActivity.newIntent(this)) }
        btnTaskColor.setOnClickListener { startActivity(TaskColorActivity.newIntent(this)) }
        btnTmdb.setOnClickListener { startActivity(TmdbActivity.newIntent(this)) }
        btnViewPager2.setOnClickListener { startActivity(ViewPager2Activity.newIntent(this)) }
        btnMoveImage.setOnClickListener { startActivity(MoveImageActivity.newIntent(this)) }
        btnConstrlayout.setOnClickListener { startActivity(ConstrLayoutActivity.newIntent(this)) }
        btnOpenCv.setOnClickListener { startActivity(OpencvActivity.newIntent(this)) }
        btnWindow.setOnClickListener { startActivity(WindowActivity.newIntent(this)) }
    }
}