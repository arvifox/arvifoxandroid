package com.arvifox.arvi.simplemisc

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.*
import com.arvifox.arvi.R
import com.arvifox.arvi.simplemisc.anim.BottomNavAnimFragment
import com.arvifox.arvi.simplemisc.anim.ExitAnim.findLocationOfCenterOnTheScreen
import com.arvifox.arvi.simplemisc.anim.ExitAnimFragment
import com.arvifox.arvi.simplemisc.anim.FabAnimFragment
import com.arvifox.arvi.simplemisc.anim.rev.AteneFragment
import com.arvifox.arvi.simplemisc.camera.camerax.CamexFragment
import com.arvifox.arvi.simplemisc.misc2.BazDto
import com.arvifox.arvi.simplemisc.misc2.Misc2Fragment1
import com.arvifox.arvi.simplemisc.misc2.approxsensor.ApproxSensorFragment
import com.arvifox.arvi.simplemisc.misc2.fragment2.Misc2Fragment2
import com.arvifox.arvi.simplemisc.misc2.fragment3.Misc2Fragment3
import com.arvifox.arvi.simplemisc.misc2.packlist.PackListFragment
import com.arvifox.arvi.simplemisc.recypad.RecyPadFragment
import com.arvifox.arvi.simplemisc.webviewbug.WebViewBugFragment
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
        externalMediaDirs
        misc2_recycler.setHasFixedSize(true)
        misc2_recycler.itemAnimator = DefaultItemAnimator()
//        misc2_recycler.itemAnimator = MyItemAnimator()
        misc2_recycler.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
//        misc2_recycler.layoutManager = GridLayoutManager(this, 2, RecyclerView.HORIZONTAL, false)
        misc2_recycler.addItemDecoration(DividerItemDecoration(this, RecyclerView.HORIZONTAL))
//        misc2_recycler.addItemDecoration(MyItemDecoration(30))
        misc2_recycler.adapter = RecAdapter(
            this, arrayListOf(
                BazDto(0, 3.2),
                BazDto(1, 2.1),
                BazDto(2, 2.1),
                BazDto(3, 2.1),
                BazDto(4, 2.1),
                BazDto(5, 22.1),
                BazDto(6, 8.8),
                BazDto(7, 7.77),
                BazDto(8, 7.88),
                BazDto(9, 7.12),
                BazDto(10, 6.23),
                BazDto(11, 2.83),
            )
        )
    }

    override fun onClick(p: Int, item: BazDto, v: View) {
        this.showToast("pos=$p, item=${item.per}")
        when (p) {
            0 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                Misc2Fragment1.newInstance(),
                ""
            ).commit()
            1 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                Misc2Fragment2.newInstance(),
                ""
            ).commit()
            2 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                Misc2Fragment3.newInstance(),
                ""
            ).commit()
            3 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                ExitAnimFragment.newInstance(v.findLocationOfCenterOnTheScreen()),
                ""
            ).commit()
            4 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                FabAnimFragment.newInstance(),
                ""
            ).commit()
            5 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                ApproxSensorFragment.newInstance(),
                ""
            ).commit()
            6 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                BottomNavAnimFragment.newInstance(),
                ""
            ).commit()
            7 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                WebViewBugFragment.newInstance(),
                ""
            ).commit()
            8 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                RecyPadFragment.newInstance(),
                ""
            ).commit()
            9 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                PackListFragment.newInstance(),
                ""
            ).commit()
            10 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                CamexFragment(),
                ""
            ).commit()
            11 -> supportFragmentManager.beginTransaction().replace(
                R.id.misc2_frame,
                AteneFragment(),
                "atene"
            ).commit()
        }
    }

    override fun onPause() {
        Log.d("foxx", "sma 2 on pause")
        super.onPause()
    }

    override fun onStop() {
        Log.d("foxx", "sma 2 on stop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("foxx", "sma 2 on destroy")
        super.onDestroy()
    }
}

class RecAdapter(private val l: OnClickListener, private val d: List<BazDto>) :
    RecyclerView.Adapter<RecAdapter.RecViewHolder>() {

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

    override fun getItemViewType(position: Int): Int {
        return position % 2
    }

    inner class RecViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv = v.findViewById<AppCompatTextView>(R.id.tvMisc2)
        val like = v.findViewById<AppCompatImageView>(R.id.ivLike)

        init {
            v.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) l.onClick(
                    adapterPosition,
                    d[adapterPosition],
                    it
                )
            }
            v.setOnLongClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    notifyItemChanged(adapterPosition, "tra")
                    true
                } else {
                    false
                }
            }
        }
    }
}

class MyItemDecoration(val d: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lp = view.layoutParams as GridLayoutManager.LayoutParams
        outRect.top = d
        if (lp.spanIndex % 2 == 0) {
            outRect.left = d
            outRect.right = d / 2
        } else {
            outRect.left = d / 2
            outRect.right = d
        }
    }
}

class MyItemAnimator : DefaultItemAnimator() {

    private val dec = DecelerateInterpolator()

    override fun recordPreLayoutInformation(
        state: RecyclerView.State,
        viewHolder: RecyclerView.ViewHolder, changeFlags: Int,
        payloads: MutableList<Any>
    ): ItemHolderInfo {
        if (changeFlags == RecyclerView.ItemAnimator.FLAG_CHANGED) {
            for (p in payloads) {
                if (p is String) {
                    return MyHolderInfo(p)
                }
            }
        }
        return super.recordPreLayoutInformation(state, viewHolder, changeFlags, payloads)
    }

    override fun canReuseUpdatedViewHolder(
        viewHolder: RecyclerView.ViewHolder,
        payloads: MutableList<Any>
    ): Boolean {
        return true
    }

    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder,
        newHolder: RecyclerView.ViewHolder, preInfo: ItemHolderInfo,
        postInfo: ItemHolderInfo
    ): Boolean {
        if (preInfo is MyHolderInfo) {
            if (preInfo.s.equals("tra", true)) {
                anim(newHolder as RecAdapter.RecViewHolder)
            }
        }
        return super.animateChange(oldHolder, newHolder, preInfo, postInfo)
    }

    private fun anim(h: RecAdapter.RecViewHolder) {
        h.like.visibility = View.VISIBLE
        h.like.scaleX = 0.0f
        h.like.scaleY = 0.0f
        val ani = AnimatorSet()
        val sli = ObjectAnimator.ofPropertyValuesHolder(
            h.like,
            PropertyValuesHolder.ofFloat("scaleX", 0f, 2f),
            PropertyValuesHolder.ofFloat("scaleY", 0f, 2f),
            PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f, 0.0f)
        )
        sli.interpolator = dec
        sli.duration = 1000
        val slit = ObjectAnimator.ofPropertyValuesHolder(
            h.tv,
            PropertyValuesHolder.ofFloat("scaleX", 1f, 0.95f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 1f, 0.95f, 1f)
        )
        slit.interpolator = dec
        slit.duration = 600
        ani.playTogether(sli, slit)
        ani.start()
    }

    inner class MyHolderInfo(val s: String) : ItemHolderInfo() {
    }
}