package com.arvifox.arvi.simplemisc.misc2.models

import java.util.*

data class StationModel(var id: String, var name: String, var brand: String,
                        var addressLine: String, var postalCode: String,
                        var city: String, var lat: Double, var lon: Double,
                        val fuelPrices: MutableList<PriceModel>,
                        var currency: String, val openingHours: MutableList<OpeningDayModel>,
                        var timeZone: String) {
    constructor() : this("", "", "", "", "", "", 0.0, 0.0,
            mutableListOf<PriceModel>(), "", mutableListOf<OpeningDayModel>(), "")
}

data class OpeningDayModel(val weekday: Int, val openingTime: Date, val closingTime: Date)

data class PriceModel(val type: String, val price: String, val lastUpdated: Date)