package com.youxiang8727.myapplication

import android.app.Application
import com.elvishew.xlog.XLog

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        XLog.init()
    }
}