package com.arvifox.arvi.domain.deleg

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.arvifox.arvi.utils.ifNull
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object sf {

    fun SharedPreferences.string(
        defaultValue: String = "",
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, String> =
        object : ReadWriteProperty<Any, String> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = getString(key(property), defaultValue).ifNull { "" }

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: String
            ) = edit().putString(key(property), value).apply()
        }

    fun SharedPreferences.int(
        defaultValue: Int = 0,
        key: (KProperty<*>) -> String = KProperty<*>::name
    ): ReadWriteProperty<Any, Int> =
        object : ReadWriteProperty<Any, Int> {
            override fun getValue(
                thisRef: Any,
                property: KProperty<*>
            ) = getInt(key(property), defaultValue)

            override fun setValue(
                thisRef: Any,
                property: KProperty<*>,
                value: Int
            ) = edit().putInt(key(property), value).apply()
        }

    class Settings(context: Context) {

        private val prefs: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(context)

        var param2 by prefs.int()
        var param3 by prefs.string(
            key = { "KEY_PARAM3" },
            defaultValue = "default"
        )
    }
}