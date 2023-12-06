package com.arvifox.arvi.simplemisc.misc2

import android.os.Parcel
import android.os.Parcelable

data class FooDto constructor(
        var das: Int,
        var qsx: Double,
        var inper: BazDto,
        var inlis: List<BazDto>? = null
) : Parcelable {

    private constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readDouble(),
            parcel.readParcelable<BazDto>(BazDto::class.java.classLoader)!!,
            mutableListOf<BazDto>().apply { parcel.readList(this as List<BazDto>, BazDto::class.java.classLoader) })

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(das)
        dest.writeDouble(qsx)
        dest.writeParcelable(inper, flags)
        dest.writeList(inlis)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<FooDto> = object : Parcelable.Creator<FooDto> {
            override fun createFromParcel(source: Parcel): FooDto {
                return FooDto(source)
            }

            override fun newArray(size: Int): Array<FooDto?> {
                return arrayOfNulls(size)
            }
        }
    }
}