package com.youxiang8727.myapplication

import android.app.Application
import com.elvishew.xlog.XLog
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        XLog.init()
    }
}