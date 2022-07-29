package com.arvifox.arvi.simplemisc.matcomp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arvifox.arvi.databinding.ActivityTaskColorBinding

class TaskColorActivity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, TaskColorActivity::class.java)
        }
    }

    private lateinit var binding: ActivityTaskColorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskColorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.incBottom.actionLayer.isActivated = false
        binding.incBottom.actionText.isActivated = false

        binding.btnTaskColor1.setOnClickListener {
            binding.incBottom.actionLayer.isActivated = !binding.incBottom.actionLayer.isActivated
        }
        binding.btnTaskColor2.setOnClickListener {
            binding.incBottom.actionText.isActivated = !binding.incBottom.actionText.isActivated
        }
    }
}
