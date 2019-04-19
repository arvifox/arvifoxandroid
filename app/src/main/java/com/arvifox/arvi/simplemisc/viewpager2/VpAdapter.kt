package com.arvifox.arvi.simplemisc.viewpager2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.item_viewpager_test.view.*

class VpAdapter : RecyclerView.Adapter<VpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VpViewHolder =
            VpViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_viewpager_test, parent, false))

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: VpViewHolder, position: Int) = holder.itemView.run {
        tvTitle.text = "sldkfj $position"
    }
}

class VpViewHolder(view: View) : RecyclerView.ViewHolder(view)