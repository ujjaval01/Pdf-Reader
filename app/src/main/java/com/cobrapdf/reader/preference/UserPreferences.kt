package com.cobrapdf.reader.preference

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private var preferences: SharedPreferences? = null

    init {
        preferences = context.getSharedPreferences(MY_PREFS_NAME, 0)
    }

    fun edit(): SharedPreferences.Editor? {
        return preferences?.edit()
    }


    var doNotAskAgain by preferences?.booleanPreference(key_do_not_ask_again, false)!!
    var quality by preferences?.booleanPreference(key_quality, false)!!
    var animation by preferences?.booleanPreference(key_animation, true)!!
    var rememberPage by preferences?.booleanPreference(key_remember_page, true)!!
    var theme by preferences?.intPreference(key_theme,0)!!
}


const val MY_PREFS_NAME = "pref_pdf_reader"

const val key_do_not_ask_again = "key_do_not_ask_again"
const val key_quality = "key_quality"
const val key_animation = "key_animation"
const val key_remember_page = "key_remember_page"
const val key_theme = "key_theme"

