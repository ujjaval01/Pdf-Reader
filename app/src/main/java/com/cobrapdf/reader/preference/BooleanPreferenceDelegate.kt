package com.cobrapdf.reader.preference

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

private class BooleanPreferenceDelegate( private val name: String,
                                 private val defaultValue: Boolean,
                                 private val preferences: SharedPreferences
):ReadWriteProperty<Any,Boolean> {
    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.edit().putBoolean(name, value).apply()
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
          return  preferences.getBoolean(name, defaultValue)
    }
}


fun SharedPreferences.booleanPreference(name: String, defaultValue: Boolean): ReadWriteProperty<Any, Boolean> = BooleanPreferenceDelegate(name, defaultValue, this)
