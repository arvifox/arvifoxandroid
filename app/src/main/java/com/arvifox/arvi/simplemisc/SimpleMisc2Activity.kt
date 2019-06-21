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
import com.arvifox.arvi.simplemisc.misc2.BazDto
import com.arvifox.arvi.utils.FormatUtils.showToast
import kotlinx.android.synthetic.main.activity_simple_misc2.*
import kotlinx.android.synthetic.main.app_bar_layout.*

class SimpleMisc2Activity : AppCompatActivity(), RecAdapter.OnClickListener {

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
        misc2_recycler.adapter = RecAdapter(this, arrayListOf(BazDto(2, 3.2), BazDto(3, 2.1)))
    }

    override fun onClick(p: Int, item: BazDto, v: View) {
        this.showToast("pos=$p, item=${item.per}")
    }
}

class RecAdapter(private val l: OnClickListener, private val d: List<BazDto>) : RecyclerView.Adapter<RecAdapter.RecViewHolder>() {

    interface OnClickListener {
        fun onClick(p: Int, item: BazDto, v: View)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val bi = inflater.inflate(R.layout.item_misc2, parent, false)
//            bi.setOnClickListener(object : View.OnClickListener {
//                override fun onClick(v: View?) {
//                }
//            })
        return RecViewHolder(bi)
    }

    override fun getItemCount(): Int = d.size

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        holder.tv.text = "baz=${d[position].per}"
    }

    inner class RecViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<AppCompatTextView>(R.id.tvMisc2)

        init {
            v.setOnClickListener { l.onClick(adapterPosition, d[adapterPosition], it) }
        }
    }

}