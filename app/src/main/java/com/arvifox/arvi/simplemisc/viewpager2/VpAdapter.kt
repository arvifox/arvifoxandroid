package com.arvifox.arvi.simplemisc.viewpager2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.arvifox.arvi.databinding.ItemViewpagerTestBinding

class VpAdapter : RecyclerView.Adapter<VpViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VpViewHolder =
        VpViewHolder(
            ItemViewpagerTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun getItemCount(): Int = 5

    override fun onBindViewHolder(holder: VpViewHolder, position: Int) {
        holder.t.text = "sldkfj $position"
    }
}

class VpViewHolder(view: ItemViewpagerTestBinding) : RecyclerView.ViewHolder(view.root) {
    val t = view.tvTitle
}