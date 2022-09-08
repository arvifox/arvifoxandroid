package com.arvifox.arvi.simplemisc.misc2

import android.os.Parcel
import android.os.Parcelable

data class BazDto constructor(
        var per: Int,
        var wer: Double
) : Parcelable {

    private constructor(parcel: Parcel) : this(parcel.readInt(), parcel.readDouble())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(per)
        dest.writeDouble(wer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<BazDto> = object : Parcelable.Creator<BazDto> {
            override fun createFromParcel(source: Parcel): BazDto {
                return BazDto(source)
            }

            override fun newArray(size: Int): Array<BazDto?> {
                return arrayOfNulls(size)
            }
        }
    }
}