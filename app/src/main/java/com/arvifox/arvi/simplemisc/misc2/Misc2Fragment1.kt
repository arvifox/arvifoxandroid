package com.arvifox.arvi.simplemisc.misc2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.arvifox.arvi.R
import com.arvifox.arvi.rep.RetrofitMock
import com.arvifox.arvi.simplemisc.misc2.models.DaResponse
import com.arvifox.arvi.utils.AssetUtils
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_testcustom.*
import retrofit2.Call
import retrofit2.Callback

class Misc2Fragment1 : Fragment() {

    companion object {
        fun newInstance(): Misc2Fragment1 {
            return Misc2Fragment1()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_testcustom, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        misc2_btn.setOnClickListener {
            val typemy = object : TypeToken<List<Tomapin>>() {}.type
            val mpr: List<Tomapin> = AssetUtils.loadGsonFromAssets(context!!, "mapping.json", typemy)
            val s = mpr.size
        }
        misc2_btn2.setOnClickListener {
            val r = RetrofitMock.getR(AssetUtils.loadStringFromAssets(context!!, "2.json")).getRe()
            r.enqueue(object : Callback<DaResponse> {
                override fun onFailure(call: Call<DaResponse>, t: Throwable) {
                    var uu = 23
                }

                override fun onResponse(call: Call<DaResponse>, response: retrofit2.Response<DaResponse>) {
                    val rr = response.body()
                    val rs = rr?.value?.size
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
    }

    override fun onOptionsMenuClosed(menu: Menu) {
        super.onOptionsMenuClosed(menu)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }
}