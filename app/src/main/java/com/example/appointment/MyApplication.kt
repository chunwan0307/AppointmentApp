package com.example.appointment

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()// Initialize the ThreeTenABP library
        AndroidThreeTen.init(this)
    }
}
    