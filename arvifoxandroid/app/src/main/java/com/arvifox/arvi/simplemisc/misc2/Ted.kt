package com.arvifox.arvi.simplemisc.misc2

import com.arvifox.arvi.simplemisc.misc2.models.PriceModel
import com.google.gson.internal.LinkedTreeMap
import java.text.SimpleDateFormat
import java.util.*

object Ted {

    @Throws(Exception::class)
    fun kdslf(list: MutableList<PriceModel>, `object`: Any) {
        if (`object` is List<*>) {
            for (pri in `object`) {
                if (pri is LinkedTreeMap<*, *>) {
                    pri as LinkedTreeMap<String, Any>
                    if (pri.containsKey("value")) {
                        val v = pri.get("value")
                        if (v is List<*>) {
                            var dp = Date()
                            var kind = ""
                            var gpr = ""
                            for (vv in (v as List<*>?)!!) {
                                if (vv is LinkedTreeMap<*, *>) {
                                    vv as LinkedTreeMap<String, Any>
                                    if ((vv.get("name") as String).equals("date_price", ignoreCase = true)) {
                                        dp = SimpleDateFormat("yyyy-MM-dd HH:m:s", Locale.getDefault()).parse(vv.get("value") as String?)
                                    }
                                    if ((vv.get("name") as String).equals("gaz_kind", ignoreCase = true)) {
                                        kind = vv.get("value") as String
                                    }
                                    if ((vv.get("name") as String).equals("price", ignoreCase = true)) {
                                        gpr = vv.get("value") as String
                                    }
                                }
                            }
                            list.add(PriceModel(kind, gpr, dp))
                        }
                    }
                }
            }
        }
    }
}
