package com.arvifox.arvi.simplemisc.misc2.approxsensor

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R
import kotlinx.android.synthetic.main.fragment_approx_sensor.*

class ApproxSensorFragment : Fragment() {

    companion object {
        fun newInstance(): ApproxSensorFragment {
            return ApproxSensorFragment()
        }
    }

    private lateinit var pm: PowerManager
    private var wwl: PowerManager.WakeLock? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        pm = context?.getSystemService(Context.POWER_SERVICE) as PowerManager
        return inflater.inflate(R.layout.fragment_approx_sensor, container, false)
    }

    @SuppressLint("NewApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        btnApproxOn1.setOnClickListener {
//            val wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "arvifox:approx")
//            wl.acquire() // switch off
//            wl.release() // switch on
//        }
        btnApproxOn1.setOnClickListener {
            wwl = if (pm.isWakeLockLevelSupported(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK)) {
                pm.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "arvifox:appwake")
            } else {
                null
            }
            wwl?.setReferenceCounted(false)
            wwl?.acquire(1000 * 60 * 2)
        }
        btnApproxOff1.setOnClickListener {
            wwl?.release()
        }
    }

    override fun onPause() {
        wwl?.release()
        super.onPause()
    }

    private fun screen() {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val w = activity?.window
        val att = w?.attributes
        att?.screenBrightness = 0F
        w?.attributes = att

        Settings.System.putInt(activity?.contentResolver, Settings.System.SCREEN_OFF_TIMEOUT, 10)
    }

}
