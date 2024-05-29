package com.youxiang8727.myapplication.ui.camerax

sealed class AnalysisResultDrawableType {
    object None: AnalysisResultDrawableType()

    object Block: AnalysisResultDrawableType()

    object Line: AnalysisResultDrawableType()
}