package com.arvifox.arvi.domain.deleg

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object Delega01 {

    class TrimDelegate : ReadWriteProperty<Any?, String> {

        private var trimmedValue: String = ""

        override fun getValue(
            thisRef: Any?,
            property: KProperty<*>
        ): String {
            return trimmedValue
        }

        override fun setValue(
            thisRef: Any?,
            property: KProperty<*>, value: String
        ) {
            trimmedValue = value.trim()
        }
    }

    class Example {

        var param: String by TrimDelegate()
    }

    class Example2 {

        private val delegate = TrimDelegate()
        var param: String
            get() = delegate.getValue(this, ::param)
            set(value) {
                delegate.setValue(this, ::param, value)
            }
    }
}