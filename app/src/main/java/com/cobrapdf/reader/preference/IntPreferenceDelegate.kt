package com.cobrapdf.reader.preference

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class IntPreferenceDelegate(
    private val name: String,
    private val defaultValue: Int?,
    private val preferences: SharedPreferences?
) : ReadWriteProperty<Any, Int> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Int {
        return preferences?.getInt(name, defaultValue!!)!!
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) {
        preferences?.edit()?.putInt(name, value)?.apply()
    }

}

fun SharedPreferences.intPreference(name: String, defaultValue: Int): ReadWriteProperty<Any, Int> =
    IntPreferenceDelegate(name, defaultValue, this)