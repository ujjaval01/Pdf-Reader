package com.cobrapdf.reader.utils

import android.content.Context
import com.cobrapdf.reader.R
import com.cobrapdf.reader.preference.UserPreferences

fun getAppTheme(context: Context): Int {
    return when (UserPreferences(context).theme) {
        0 -> R.style.AppTheme
        1-> R.style.AppTheme_Dark
        else -> R.style.AppTheme
    }
}