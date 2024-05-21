package com.youxiang.accessibilityservicehelper.model

sealed class SearchType(val isContains: Boolean) {

    class Text(isContains : Boolean): SearchType(isContains)

    class Id(isContains : Boolean): SearchType(isContains)

    class ClassName(isContains : Boolean): SearchType(isContains)

    class ContentDescription(isContains : Boolean): SearchType(isContains)
}