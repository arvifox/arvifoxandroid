package com.arvifox.arvi.domain.parcelize

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.*
import java.util.*

@Parcelize
class Person(val name: String, val age: Int) : Parcelable

@Parcelize
class Book(val title: String, val author: Person) : Parcelable {
    @IgnoredOnParcel
    var readPages: Int = 0
}

enum class State {
    ON, OFF
}

@Parcelize
class PowerSwitch(var state: State) : Parcelable

@Parcelize
class Library(val books: List<Book>) : Parcelable

object DateParceler : Parceler<Date> {

    override fun create(parcel: Parcel) = Date(parcel.readLong())

    override fun Date.write(parcel: Parcel, flags: Int) = parcel.writeLong(time)
}

@Parcelize
@TypeParceler<Date, DateParceler>
class Session(
    val title: String,
    val startTime: Date,
    val endTime: Date
) : Parcelable

@Parcelize
class Session2(
    val title: String,
    val startTime: @WriteWith<DateParceler> Date,
    val endTime: @WriteWith<DateParceler> Date
) : Parcelable