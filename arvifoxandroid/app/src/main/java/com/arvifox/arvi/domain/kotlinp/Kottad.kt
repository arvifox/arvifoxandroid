package com.arvifox.arvi.domain.kotlinp

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import kotlin.time.Duration.Companion.minutes
import kotlin.time.ExperimentalTime

@ExperimentalTime
@RequiresApi(Build.VERSION_CODES.O)
object Kottad {

    fun javaTimeApi() {
        val date = LocalDate.now()
        val r = date + Duration.ofDays(1)
        //val d: Instant = java.time.Clock.System.now()
        val j = Instant.ofEpochMilli(23947)
        val df = Instant.parse("2020-04-04T11:12:28.204Z")
//        val tt = TimeZone.getTimeZone()
//        val we = j.toLocalDateTime()
        val min = 1.minutes
//        val sdf = LocalDate(2020, 2, 4)
        //sdf.plus(23, DateTi)
    }

    fun kotdate() {

    }
}