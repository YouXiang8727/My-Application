package com.youxiang.accessibilityservicehelper.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.youxiang.accessibilityservicehelper.accessibilityService
import com.youxiang.accessibilityservicehelper.rootNodeInfo

class MyAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        accessibilityService = this
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        rootNodeInfo = rootInActiveWindow
    }

    override fun onInterrupt() {
        accessibilityService = null
    }
}