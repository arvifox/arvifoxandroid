package com.arvifox.arvi.simplemisc.misc2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import androidx.fragment.app.Fragment
import com.arvifox.arvi.databinding.FragmentTestcustomBinding
import com.arvifox.arvi.rep.RetrofitMock
import com.arvifox.arvi.simplemisc.misc2.models.DaResponse
import com.arvifox.arvi.simplemisc.misc2.models.OpeningDayModel
import com.arvifox.arvi.simplemisc.misc2.models.PriceModel
import com.arvifox.arvi.simplemisc.misc2.models.StationModel
import com.arvifox.arvi.utils.AssetUtils
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class Misc2Fragment1 : Fragment() {

    companion object {
        fun newInstance(): Misc2Fragment1 {
            return Misc2Fragment1()
        }
    }

    private lateinit var binding: FragmentTestcustomBinding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTestcustomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.misc2Btn.setOnClickListener {
            val typemy = object : TypeToken<List<Tomapin>>() {}.type
            val mpr: List<Tomapin> =
                AssetUtils.loadGsonFromAssets(requireContext(), "mapping.json", typemy)
            val s = mpr.size
        }
        binding.misc2Btn2.setOnClickListener {
            val r = RetrofitMock.getR(AssetUtils.loadStringFromAssets(requireContext(), "2.json"))
                .getRe()
            r.enqueue(object : Callback<DaResponse> {
                override fun onFailure(call: Call<DaResponse>, t: Throwable) {
                    var uu = 23
                }

                override fun onResponse(
                    call: Call<DaResponse>,
                    response: retrofit2.Response<DaResponse>
                ) {
                    val rr = response.body()
                    val rs = mapResponseToGasStationModel(rr!!)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
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

    //region private

    private fun mapResponseToGasStationModel(response: DaResponse): List<StationModel> {
        val result = mutableListOf<StationModel>()
        for (r in response.value) {
            for (station in r.value) {
                val stationModel = StationModel()
                station.value.forEach {
                    when (it.name) {
                        "uuid" -> stationModel.id = it.value as String
                        "station_name" -> stationModel.name = it.value as String
                        "brand" -> stationModel.brand = it.value as String
                        "address" -> stationModel.addressLine = it.value as String
                        "zipcode" -> stationModel.postalCode = it.value as String
                        "locality" -> stationModel.city = it.value as String
                        "latitude" -> stationModel.lat =
                            NumberFormat.getNumberInstance(Locale.getDefault())
                                .parse((it.value as String).replace(".", ",")).toDouble()
                        "longitude" -> stationModel.lon =
                            NumberFormat.getNumberInstance(Locale.getDefault())
                                .parse((it.value as String).replace(".", ",")).toDouble()
                        "currency" -> stationModel.currency = it.value as String
                        "timezone" -> stationModel.timeZone = it.value as String
                        "hours" -> parseOpeningHours(stationModel.openingHours, it.value as String)
                        "gaz_prices" -> parseGasPrices(stationModel.fuelPrices, it.value)
                    }
                }
                result.add(stationModel)
            }
        }
        return result
    }

    private fun parseOpeningHours(list: MutableList<OpeningDayModel>, hours: String) {
        if (TextUtils.isEmpty(hours)) return
        val s1 = hours.split("]").map { it.replace("[", "", true) }
        for (s in s1) {
            if (TextUtils.isEmpty(s)) continue
            var d: Int = 1
            var d2: Int = 1
            var o1 = Date()
            var c2 = Date()
            s.split(" ").forEachIndexed { index, ss ->
                if (index == 0) {
                    if (ss.length == 1) {
                        d = ss.toInt()
                    } else {
                        ss.split("-").forEachIndexed { index2, s2 ->
                            if (index2 == 0) d = s2.toInt() else d2 = s2.toInt()
                        }
                    }
                } else {
                    ss.split("-").forEachIndexed { index2, s2 ->
                        if (index2 == 0) o1 =
                            SimpleDateFormat("H:m", Locale.getDefault()).parse(s2) else
                            c2 = SimpleDateFormat("H:m", Locale.getDefault()).parse(s2)
                    }
                }
            }
            for (days in d..d2) {
                list.add(OpeningDayModel(days, o1, c2))
            }
        }
    }

    private fun parseGasPrices(list: MutableList<PriceModel>, gasPrices: Any) {
        Ted.kdslf(list, gasPrices)
        if (gasPrices is List<*>) {
            for (pri in gasPrices) {
                val p: PriceModel
                if (pri is LinkedTreeMap<*, *>) {

                }
            }
        }
    }

    //endregion
}