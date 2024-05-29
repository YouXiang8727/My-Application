package com.youxiang8727.myapplication.ui.camerax

import android.util.Range
import com.youxiang8727.myapplication.Action
import com.youxiang8727.myapplication.Reducer
import com.youxiang8727.myapplication.State

data class ComposeCameraState(
    val isPermissionGranted: Boolean = false,
    val analysisResultDrawableType: AnalysisResultDrawableType = AnalysisResultDrawableType.None,
    val analysisResultConfidenceRange: Range<Float> = Range(0f, 1f)
): State {

}

sealed interface ComposeCameraAction: Action {
    class SetPermissionGranted(val isGranted: Boolean) : ComposeCameraAction

    class SetAnalysisResultDrawableType(val analysisResultDrawableType: AnalysisResultDrawableType) : ComposeCameraAction

    class SetAnalysisResultConfidenceRange(val analysisResultConfidenceRange: Range<Float>) : ComposeCameraAction
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

        }
    }
}

