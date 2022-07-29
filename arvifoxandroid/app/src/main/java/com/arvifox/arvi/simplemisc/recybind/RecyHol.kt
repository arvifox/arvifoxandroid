package com.arvifox.arvi.simplemisc.recybind

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class SimpleAdapter<T>(
    @LayoutRes val layout: Int, val items: List<T>,
    val vari: Int, val variClick: Int, val coolOnClick: (View, T, Int) -> Unit
) :
    RecyclerView.Adapter<SimpleViewHolder<*>>() {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder<*> {
        val inf = LayoutInflater.from(parent.context)
        val bin: ViewDataBinding = DataBindingUtil.inflate(inf, layout, parent, false)
        return SimpleViewHolder(bin).apply {
            vh.setVariable(variClick, object : SimpleOnItemClick {
                override fun onClick(v: View, item: Any) {
                    coolOnClick(v, item as T, adapterPosition)
                }
            })
        }
    }

    override fun onBindViewHolder(holder: SimpleViewHolder<*>, position: Int) {
        holder.vh.setVariable(vari, items[position])
        holder.vh.executePendingBindings()
    }

    interface SimpleOnItemClick {
        fun onClick(v: View, item: Any)
    }

}

class SimpleViewHolder<VH : ViewDataBinding>(val vh: VH) :
    RecyclerView.ViewHolder(vh.root)