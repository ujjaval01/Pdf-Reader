package com.cobrapdf.reader.preference

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class StringPreferenceDelegate(
    private val name: String,
    private val defaultValue: String?,
    private val preferences: SharedPreferences?
) : ReadWriteProperty<Any, String?>{
    override fun getValue(thisRef: Any, property: KProperty<*>): String? =
        preferences?.getString(name,defaultValue)!!

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences?.edit()?.putString(name, value)?.apply()
    }
}

fun SharedPreferences?.stringPreference(name: String, defaultValue: String?): ReadWriteProperty<Any, String?> = StringPreferenceDelegate(name, defaultValue, this)
