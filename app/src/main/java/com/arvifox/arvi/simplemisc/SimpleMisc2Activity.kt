package com.arvifox.arvi.simplemisc

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.activity_simple_misc2.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class SimpleMisc2Activity : AppCompatActivity() {

    companion object {
        fun newIntent(c: Context): Intent {
            return Intent(c, SimpleMisc2Activity::class.java)
        }
    }

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_misc2)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        misc2_recycler.setHasFixedSize(true)
        misc2_recycler.itemAnimator = DefaultItemAnimator()
        misc2_recycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        misc2_recycler.adapter = RecAdapter()
    }

    class RecViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<AppCompatTextView>(R.id.tvMisc2)
    }

    class RecAdapter : RecyclerView.Adapter<RecViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            val bi = inflater.inflate(R.layout.item_misc2, parent, false)
            return RecViewHolder(bi)
        }

        override fun getItemCount(): Int = 15

        override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
            holder.tv.text = "pos-$position"
        }
    }
}