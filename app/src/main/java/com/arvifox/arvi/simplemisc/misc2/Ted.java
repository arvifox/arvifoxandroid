package com.arvifox.arvi.simplemisc.misc2;

import com.arvifox.arvi.simplemisc.misc2.models.PriceModel;
import com.google.gson.internal.LinkedTreeMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Ted {

    public static void kdslf(List<PriceModel> list, Object object) throws Exception {
        if (object instanceof List) {
            for (Object pri : (List) object) {
                if (pri instanceof LinkedTreeMap) {
                    if (((LinkedTreeMap) pri).containsKey("value")) {
                        Object v = ((LinkedTreeMap) pri).get("value");
                        if (v instanceof List) {
                            Date dp = new Date();
                            String kind = "";
                            String gpr = "";
                            for (Object vv : (List) v) {
                                if (vv instanceof LinkedTreeMap) {
                                    LinkedTreeMap vvv = (LinkedTreeMap) vv;
                                    if (((String) vvv.get("name")).equalsIgnoreCase("date_price")) {
                                        dp = new SimpleDateFormat("yyyy-MM-dd HH:m:s", Locale.getDefault()).parse((String) vvv.get("value"));
                                    }
                                    if (((String) vvv.get("name")).equalsIgnoreCase("gaz_kind")) {
                                        kind = (String) vvv.get("value");
                                    }
                                    if (((String) vvv.get("name")).equalsIgnoreCase("price")) {
                                        gpr = (String) vvv.get("value");
                                    }
                                }
                            }
                            list.add(new PriceModel(kind, gpr, dp));
                        }
                    }
                }
            }
        }
    }
}
