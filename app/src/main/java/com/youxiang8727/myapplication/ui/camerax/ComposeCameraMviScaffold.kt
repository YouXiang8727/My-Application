package com.youxiang8727.myapplication.ui.camerax

import android.util.Range
import androidx.annotation.IntRange
import androidx.compose.ui.graphics.Color
import com.youxiang8727.myapplication.Action
import com.youxiang8727.myapplication.Reducer
import com.youxiang8727.myapplication.State

data class ComposeCameraState(
    val isPermissionGranted: Boolean = false,
    val analysisResultDrawableType: AnalysisResultDrawableType = AnalysisResultDrawableType.None,
    val analysisResultConfidenceRange: Range<Float> = Range(0f, 1f),
    @IntRange(0, 4095)
    val analysisResultDrawableColorLerp: Int = 0
): State {
    val analysisResultDrawableColor = getColorFromIndex(analysisResultDrawableColorLerp)

    private fun getColorFromIndex(index: Int): Color {
        // 確保 index 在 0 到 511 之間
        val clampedIndex = index.coerceIn(0, 4095)

        // 計算 R、G、B 值
        val r = (clampedIndex / (16 * 16)) % 16 * 255 / 15
        val g = (clampedIndex / 16) % 16 * 255 / 15
        val b = (clampedIndex % 16) * 255 / 15

        return Color(r.toInt(), g.toInt(), b.toInt())
    }
}

sealed interface ComposeCameraAction: Action {
    class SetPermissionGranted(val isGranted: Boolean) : ComposeCameraAction

    class SetAnalysisResultDrawableType(val analysisResultDrawableType: AnalysisResultDrawableType) : ComposeCameraAction

    class SetAnalysisResultConfidenceRange(val analysisResultConfidenceRange: Range<Float>) : ComposeCameraAction

    class SetAnalysisResultDrawableColorLerp(
        @IntRange(0, 4095)
        val analysisResultDrawableColorLerp: Int
    ) : ComposeCameraAction
}

class ComposeCameraActionReducer: Reducer<ComposeCameraState, ComposeCameraAction> {
    override fun reduce(
        state: ComposeCameraState,
        action: ComposeCameraAction
    ): ComposeCameraState {
        return when (action) {
            is ComposeCameraAction.SetPermissionGranted -> {
                state.copy(
                    isPermissionGranted = action.isGranted
                )
            }

            is ComposeCameraAction.SetAnalysisResultDrawableType -> {
                state.copy(
                    analysisResultDrawableType = action.analysisResultDrawableType
                )
            }

            is ComposeCameraAction.SetAnalysisResultConfidenceRange -> {
                state.copy(
                    analysisResultConfidenceRange = action.analysisResultConfidenceRange
                )
            }

            is ComposeCameraAction.SetAnalysisResultDrawableColorLerp -> {
                state.copy(
                    analysisResultDrawableColorLerp = action.analysisResultDrawableColorLerp
                )
            }

        }
    }
}

