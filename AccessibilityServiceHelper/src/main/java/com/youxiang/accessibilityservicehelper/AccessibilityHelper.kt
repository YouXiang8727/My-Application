package com.youxiang.accessibilityservicehelper

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Path
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.youxiang.accessibilityservicehelper.model.SearchRule
import com.youxiang.accessibilityservicehelper.service.MyAccessibilityService
import java.io.File
import kotlin.math.abs

class AccessibilityHelper private constructor(
    private val context : Application
) {
    companion object {
        private var INSTANCE : AccessibilityHelper? = null
            @Synchronized get
            @Synchronized set

        fun init(
            context: Application
        ) {
            if (INSTANCE == null) {
                INSTANCE = AccessibilityHelper(context)
            }
        }

        fun getInstance() : AccessibilityHelper =
            INSTANCE ?: throw IllegalStateException("AccessibilityHelper has not be initialized")
    }

    private val targetAccessibilityServiceName = "${context.packageName}${File.separator}${MyAccessibilityService::class.java.name}"

    private val swipeCallback: AccessibilityService.GestureResultCallback =
        object : AccessibilityService.GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
            }
        }

    fun checkAccessibilityServiceEnabled(): Boolean {
        val enabledServices = Settings.Secure.getString(
            context.contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        )
        return (enabledServices == targetAccessibilityServiceName)
    }

    fun intentAccessibilityServiceSettings() {
        try {
            context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        } catch (e: Throwable) {
            try {
                val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    fun AccessibilityNodeInfo?.findNode(
        searchRules: List<SearchRule>,
        isOr: Boolean = false
    ): AccessibilityNodeInfo? {
        if (this == null) return null
        if (this.matchRules(searchRules, isOr)) return this

        for (i in 0 until this.childCount) {
            val childNode = this.getChild(i)
            childNode?.let {
                val result = it.findNode(searchRules, isOr)
                if (result != null) {
                    return result
                }
            }
        }

        return null
    }

    fun AccessibilityNodeInfo?.findNodes(
        searchRules: List<SearchRule>,
        isOr: Boolean = false
    ): List<AccessibilityNodeInfo> {
        return this?.traverseNode(searchRules, isOr) ?: emptyList()
    }

    fun AccessibilityNodeInfo?.seekNode(indexArray: IntArray): AccessibilityNodeInfo? {
        try {
            if (this == null || indexArray.isEmpty()) {
                return this
            }

            var currentNode = this
            for (index in indexArray) {
                val targetNode = if (index < 0) {
                    currentNode?.findParentNode(abs(index))
                } else {
                    currentNode?.getChild(index)
                }
                targetNode?.let {
                    currentNode = it
                } ?: return null
            }

            return currentNode

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun AccessibilityNodeInfo?.click() {
        if (accessibilityService == null) {
            return
        }
        if (this == null) {
            return
        }

        if (this.isClickable) {
            // 如果目标节点是可点击的，直接执行点击
            this.performAction(AccessibilityNodeInfo.ACTION_CLICK)
        } else {
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                this.clickAtPosition()
            }, 1000) // 延迟执行以确保正确执行
        }
    }

    fun AccessibilityNodeInfo?.inputText(textToInput: String) {
        if (this != null) {
            this.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            Thread.sleep(500)
            val arguments = Bundle()
            arguments.putCharSequence(
                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE,
                textToInput
            )
            this.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments)
            Thread.sleep(500)
            this.performAction(AccessibilityNodeInfo.ACTION_CLEAR_FOCUS)
        }
    }

    fun AccessibilityNodeInfo?.pasteText(textToPaste: String) {
        if (this == null) {
            return
        }
        if (accessibilityService == null) {
            return
        }
        val clipboard = accessibilityService!!.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("label", textToPaste)

        clipboard.setPrimaryClip(clipData)

        val result = this.performAction(AccessibilityNodeInfo.ACTION_PASTE)
    }

    fun swipe(
        fromX: Int,
        endX: Int,
        fromY: Int,
        endY: Int,
        startTime: Long,
        duration: Long,
    ) {
        if (accessibilityService == null) {
            return
        }
        try {
            val path = Path()
            path.moveTo(fromX.toFloat(), fromY.toFloat())
            path.lineTo(endX.toFloat(), endY.toFloat())
            val builder = GestureDescription.Builder()
            builder.addStroke(
                GestureDescription.StrokeDescription(
                    path,
                    startTime,
                    duration
                )
            )
            val gesture = builder.build()
            accessibilityService?.dispatchGesture(gesture, swipeCallback, null)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun clickPosition(x: Float, y: Float) {
        clickAtPosition(x, y)
    }
}