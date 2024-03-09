package com.cobrapdf.reader.ob

import android.content.Context
import android.widget.Toast

object Toast {
    fun toast(context: Context?, stringId: Int) {
        Toast.makeText(context, stringId, Toast.LENGTH_LONG).show()
    }
}