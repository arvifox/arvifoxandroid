package com.arvifox.arvi.simplemisc.misc2.models

import com.google.gson.annotations.Expose

data class DaResponse(@Expose val value: List<DaPart1>)

data class DaPart1(@Expose val value: List<DaGSt>)

data class DaGSt(@Expose val name: String, @Expose val value: List<DaGSPa>)

data class DaGSPa(@Expose val name: String, @Expose val value: Any)