package com.cobrapdf.reader.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // window?.navigationBarColor = resources?.getColor(R.color.white)!!
       // window?.statusBarColor = resources?.getColor(R.color.app_default_transparent)!!

    }
}