package com.arvifox.arvi.simplemisc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivitySimpleMiscBinding
import com.arvifox.arvi.simplemisc.anim.AnimActivity
import com.arvifox.arvi.simplemisc.camera.CameraShotActivity
import com.arvifox.arvi.simplemisc.constrlayout.ConstrLayoutActivity
import com.arvifox.arvi.simplemisc.matcomp.MatCompActivity
import com.arvifox.arvi.simplemisc.matcomp.TaskColorActivity
import com.arvifox.arvi.simplemisc.misc4window.WindowActivity
import com.arvifox.arvi.simplemisc.moveimage.MoveImageActivity
import com.arvifox.arvi.simplemisc.sensor.SensorActivity
import com.arvifox.arvi.simplemisc.servicehandler.ServiceHandlerActivity
import com.arvifox.arvi.simplemisc.tmdb.TmdbActivity
import com.arvifox.arvi.simplemisc.view.NestedScrollActivity
import com.arvifox.arvi.simplemisc.viewpager2.ViewPager2Activity
import com.arvifox.arvi.simplemisc.workmanager.WorkManSelectActivity

class SimpleMiscActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, SimpleMiscActivity::class.java)
        }
    }

    private var bi: ActivitySimpleMiscBinding? = null
    private val binding by lazy { bi!! }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bi = ActivitySimpleMiscBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.incSimpl1.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnCameraStart.setOnClickListener { startActivity(CameraShotActivity.newIntent(this)) }
        binding.btnSensor.setOnClickListener { startActivity(SensorActivity.newIntent(this)) }
        binding.btnServiceHandler.setOnClickListener {
            startActivity(
                ServiceHandlerActivity.newIntent(
                    this
                )
            )
        }
        binding.btnMatComp.setOnClickListener { startActivity(MatCompActivity.newIntent(this)) }
        binding.btnAnim.setOnClickListener { startActivity(AnimActivity.newIntent(this)) }
        binding.btnWorkManager.setOnClickListener {
            startActivity(
                WorkManSelectActivity.newIntent(
                    this
                )
            )
        }
        binding.btnNestedScroll.setOnClickListener {
            startActivity(
                NestedScrollActivity.newIntent(
                    this
                )
            )
        }
        binding.btnTaskColor.setOnClickListener { startActivity(TaskColorActivity.newIntent(this)) }
        binding.btnTmdb.setOnClickListener { startActivity(TmdbActivity.newIntent(this)) }
        binding.btnViewPager2.setOnClickListener { startActivity(ViewPager2Activity.newIntent(this)) }
        binding.btnMoveImage.setOnClickListener { startActivity(MoveImageActivity.newIntent(this)) }
        binding.btnConstrlayout.setOnClickListener {
            startActivity(
                ConstrLayoutActivity.newIntent(
                    this
                )
            )
        }
        binding.btnWindow.setOnClickListener { startActivity(WindowActivity.newIntent(this)) }
    }
}