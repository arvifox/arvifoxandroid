package com.arvifox.arvi.simplemisc.misc2

import com.google.gson.annotations.Expose

data class Tomap(val l: List<Tomapin>)

data class Tomapin(@Expose val type: String, @Expose val keys: List<String>)