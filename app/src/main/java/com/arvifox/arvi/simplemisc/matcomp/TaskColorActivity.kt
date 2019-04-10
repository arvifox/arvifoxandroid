package com.arvifox.arvi.simplemisc.matcomp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.activity_task_color.*
import kotlinx.android.synthetic.main.app_bar_layout.*
import kotlinx.android.synthetic.main.layout_bottom_toolbar.*

class TaskColorActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, TaskColorActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_color)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        actionLayer.isActivated = false
        actionText.isActivated = false

        btnTaskColor1.setOnClickListener { actionLayer.isActivated = !actionLayer.isActivated }
        btnTaskColor2.setOnClickListener { actionText.isActivated = !actionText.isActivated }
    }
}
