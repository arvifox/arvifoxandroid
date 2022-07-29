package com.arvifox.arvi.simplemisc.misc2.packlist

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ActivityManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arvifox.arvi.BR
import com.arvifox.arvi.R
import com.arvifox.arvi.simplemisc.recybind.SimpleAdapter
import com.arvifox.arvi.utils.FormatUtils.showToast
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class PackListFragment : Fragment() {

    companion object {
        fun newInstance(): PackListFragment {
            return PackListFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pack_list, container, false)
        val rv = view.findViewById<RecyclerView>(R.id.rvPackList)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rv.adapter = SimpleAdapter(
            R.layout.item_pack_list,
            getGetGet(),
            BR.varPackListItem,
            BR.varPackListItemOnClick
        ) { v, i, p -> }
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        startActivity(intent)
        return view
    }

    private fun getGetGet(): List<PackListItem> {
        val r = getInstalledApps()
            .sortedBy { it.title }
        //.filter { it.pers.contains("bluetooth_admin", true) }
        activity?.showToast("Count = ${r.size}")
        return r
    }

    private fun getApps(): List<PackListItem> {
        val i = Intent(Intent.ACTION_MAIN, null)
        i.addCategory(Intent.CATEGORY_LAUNCHER)
        val pkgs = context?.packageManager?.queryIntentActivities(i, 0) ?: return emptyList()


        return emptyList()
    }

    private fun isSystemPackage(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
    }

    private fun getLaunchIntent(f: ApplicationInfo): Intent? {
        return context?.packageManager?.getLaunchIntentForPackage(f.packageName)
    }

    private fun getInstalledApplications(): List<PackListItem> {
        val res = mutableListOf<PackListItem>()
        val applis = context?.packageManager?.getInstalledApplications(PackageManager.GET_META_DATA)
            ?: return emptyList()
        for (i in applis.indices) {
            val p = applis[i]
            val newInfo = PackListItem()
            newInfo.title = p.packageName
            newInfo.name = p.name ?: "no name"
            res.add(newInfo)
        }
        return res
    }

    private fun getInstalledApps(): List<PackListItem> {
        val res = mutableListOf<PackListItem>()
        val packs = context?.packageManager?.getInstalledPackages(PackageManager.GET_PERMISSIONS)
            ?: return emptyList()
        for (i in packs.indices) {
            val p = packs[i]
            val newInfo = PackListItem()
            newInfo.title = p.applicationInfo.loadLabel(context?.packageManager!!).toString()
            newInfo.name = p.packageName
            newInfo.ver = p.versionName
            newInfo.verc = p.versionCode
            newInfo.ic = p.applicationInfo.loadIcon(context?.packageManager!!)
            newInfo.pers = p.requestedPermissions?.joinToString(separator = ",") ?: "no pers"
            if (!isSystemPackage(p)) res.add(newInfo)
        }
        return res
    }

    private fun getRunningAp(): List<PackListItem> {
        val res = mutableListOf<PackListItem>()
        val am = context?.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningAppProcessInfo = am.runningAppProcesses

        for (i in runningAppProcessInfo.indices) {
            res.add(PackListItem(title = runningAppProcessInfo[i].processName))
        }
        return res
    }

    @SuppressLint("NewApi")
    private fun isForeground(ctx: Context, myPackage: String?): Boolean {
        val manager: ActivityManager =
            ctx.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val runningTaskInfo: List<ActivityManager.RunningTaskInfo> =
            manager.getRunningTasks(1)
        val componentInfo: ComponentName? = runningTaskInfo[0].topActivity
        return componentInfo?.packageName.equals(myPackage, true)
    }

    // https://developer.android.com/reference/android/app/usage/UsageStatsManager.html
    private fun sdlkfjlk() {
        val topPackageName: String
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            val mUsageStatsManager: UsageStatsManager = context?.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val time = System.currentTimeMillis()
            val stats: List<UsageStats> = mUsageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                time - 1000 * 10,
                time
            )
            if (stats != null) {
                val mySortedMap: SortedMap<Long, UsageStats> = TreeMap<Long, UsageStats>()
                for (usageStats in stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats)
                }
                if (!mySortedMap.isEmpty()) {
                    topPackageName = mySortedMap.get(mySortedMap.lastKey())?.getPackageName().orEmpty()
                }
            }
        }
    }
}

data class PackListItem(
    var title: String = "",
    var name: String = "",
    var ver: String = "",
    var verc: Int = 0,
    var ic: Drawable? = null,
    var pers: String = ""
)