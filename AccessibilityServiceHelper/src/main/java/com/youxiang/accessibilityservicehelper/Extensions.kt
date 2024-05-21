package com.youxiang.accessibilityservicehelper

import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo
import com.youxiang.accessibilityservicehelper.model.SearchRule
import com.youxiang.accessibilityservicehelper.model.SearchType
import com.youxiang.accessibilityservicehelper.service.MyAccessibilityService

internal var accessibilityService : MyAccessibilityService? = null

var rootNodeInfo : AccessibilityNodeInfo? = null
    @Synchronized internal set
    @Synchronized get

internal fun AccessibilityNodeInfo.matchRules(
    searchRules: List<SearchRule>,
    isOr: Boolean
): Boolean {
    try {
        searchRules.forEach {
            val keyword: String = when (it.searchType) {
                is SearchType.Id -> {
                    this.viewIdResourceName
                }
                is SearchType.Text -> {
                    this.text
                }
                is SearchType.ContentDescription -> {
                    this.contentDescription
                }
                is SearchType.ClassName -> {
                    this.className
                }
            }.run {
                this?.toString() ?: ""
            }

            val match: Boolean = if (it.searchType.isContains) {
                keyword.contains(it.key)
            }else {
                keyword == it.key
            }

            if (isOr && match) return true
            if (isOr.not() && match.not()) return false
        }
        return isOr.not()
    }catch (e: Exception) {
        e.printStackTrace()
        return false
    }
}

internal fun AccessibilityNodeInfo.traverseNode(
    searchRules: List<SearchRule>,
    isOr: Boolean,
    list: MutableList<AccessibilityNodeInfo>? = null
): MutableList<AccessibilityNodeInfo> {
    val result = list ?: mutableListOf<AccessibilityNodeInfo>()
    if (this.matchRules(searchRules, isOr)) result.add(this)
    val childCount = this.childCount
    for (i in 0 until childCount) {
        val childNode = this.getChild(i)
        childNode?.traverseNode(searchRules, isOr, result)
    }
    return result
}

internal fun AccessibilityNodeInfo?.findParentNode(depth: Int): AccessibilityNodeInfo? {
    try {
        if (this == null || depth == 0) {
            return this
        }

        return this.parent.findParentNode(depth - 1)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}

internal fun clickAtPosition(x: Float, y: Float) {
    if (accessibilityService == null) {
        return
    }
    val builder = GestureDescription.Builder()
    val p = Path()
    p.moveTo(x, y)
    builder.addStroke(GestureDescription.StrokeDescription(p, 0, 100))
    val gesture = builder.build()
    accessibilityService?.dispatchGesture(gesture, null, null)
}

internal fun AccessibilityNodeInfo?.clickAtPosition() {
    if (this == null) return

    val center = Rect()
    this.getBoundsInScreen(center)

    // 获取屏幕上的绝对位置
    val absX = center.centerX().toFloat()
    val absY = center.centerY().toFloat()

    clickAtPosition(absX, absY)
}
