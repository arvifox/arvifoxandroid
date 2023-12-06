package com.arvifox.arvi.utils.diffutil

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlin.properties.Delegates

class SampleAdapter : RecyclerView.Adapter<SampleViewHolder>() {

//    private var itemList: List<SampleItem> = listOf()

//    private var itemList: List<SampleItem> by Delegates.observable(
//        initialValue = listOf(),
//        onChange = { property, oldValue, newValue ->
//            autoNotify(oldValue, newValue)
//        })

    private var itemList: List<SampleItem> by autoNotifyDelegate(adapter = this, initialValue = listOf())

    fun setItems(newList: List<SampleItem>) {
        val diffItemCallback = object : DiffUtil.Callback() {

            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                itemList[oldItemPosition].getItemId() == newList[newItemPosition].getItemId()

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                itemList[oldItemPosition].getDiff() == newList[newItemPosition].getDiff()

            override fun getOldListSize() = itemList.size

            override fun getNewListSize() = newList.size
        }

        val diffResult = DiffUtil.calculateDiff(diffItemCallback)
        diffResult.dispatchUpdatesTo(this@SampleAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SampleViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: SampleViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class SampleItem : DiffItem {
    override fun getItemId(): String {
        return "123"
    }

    override fun getDiff(): String {
        return "34"
    }
}

class SampleViewHolder(v: View) : RecyclerView.ViewHolder(v) {

}