package com.arvifox.arvi.simplemisc.recypad

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arvifox.arvi.R
import com.arvifox.arvi.utils.FormatUtils.showToast
import kotlinx.android.synthetic.main.fragment_recy_pad.*

class RecyPadFragment : Fragment() {

    companion object {
        fun newInstance(): RecyPadFragment {
            return RecyPadFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recy_pad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvRecypad.setHasFixedSize(true)
        rvRecypad.itemAnimator = DefaultItemAnimator()
        rvRecypad.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvRecypad.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        rvRecypad.adapter = RecypadAdapter(
            arrayListOf(
                "qwe",
                "sdf",
                "sfsff",
                "vbvbv",
                "ioiuo",
                "sdsfff",
                "uweioq",
                "pocpocxz",
                "xcnmdsad",
                "qwe",
                "sdf",
                "sfsff",
                "vbvbv",
                "ioiuo",
                "sdsfff",
                "uweioq",
                "pocpocxz",
                "xcnmdsad"
            )
        )
        tvRecypad.setOnClickListener {
            activity?.showToast("click nul")
        }
    }

}

class RecypadAdapter(private val d: List<String>) :
    RecyclerView.Adapter<RecypadAdapter.RecViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val bi = inflater.inflate(R.layout.item_recypad, parent, false)
        return RecViewHolder(bi)
    }

    override fun getItemCount(): Int = d.size

    override fun onBindViewHolder(holder: RecViewHolder, position: Int) {
        holder.tv.text = "baz=${d[position]}"
    }

    inner class RecViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<AppCompatTextView>(R.id.tvRecypadItem)
    }
}
