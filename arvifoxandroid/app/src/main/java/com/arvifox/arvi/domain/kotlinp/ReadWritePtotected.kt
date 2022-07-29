package com.arvifox.arvi.domain.kotlinp

object ReadWritePtotected {

    open class Read {
        open val value: String = "hel"
    }

    open class WriteProtected : Read() {
        override var value: String = super.value
            protected set

        fun append(s: String) {
            value += s
        }
    }

    fun sdflkdsjf() {
        val dd = WriteProtected()
        //dd.value = "dkfj" //cannot assign to value
        dd.append("sldjf")
    }
}