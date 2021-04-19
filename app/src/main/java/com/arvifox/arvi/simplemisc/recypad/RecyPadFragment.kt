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
import com.arvifox.arvi.databinding.FragmentRecyPadBinding
import com.arvifox.arvi.utils.FormatUtils.showToast

class RecyPadFragment : Fragment() {

    companion object {
        fun newInstance(): RecyPadFragment {
            return RecyPadFragment()
        }
    }

    private lateinit var binding: FragmentRecyPadBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRecyPadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvRecypad.setHasFixedSize(true)
        binding.rvRecypad.itemAnimator = DefaultItemAnimator()
        binding.rvRecypad.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvRecypad.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        binding.rvRecypad.adapter = RecypadAdapter(
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
        binding.tvRecypad.setOnClickListener {
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
