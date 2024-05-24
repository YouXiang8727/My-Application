package com.youxiang8727.myapplication.ui.camerax

import com.youxiang8727.myapplication.Action
import com.youxiang8727.myapplication.Reducer
import com.youxiang8727.myapplication.State

data class ComposeCameraState(
    val isPermissionGranted: Boolean = false
): State {

}

sealed interface ComposeCameraAction: Action {
    class SetPermissionGranted(val isGranted: Boolean) : ComposeCameraAction
}

class ComposeCameraActionReducer: Reducer<ComposeCameraState, ComposeCameraAction> {
    override fun reduce(
        state: ComposeCameraState,
        action: ComposeCameraAction
    ): ComposeCameraState {
        return when (action) {
            is ComposeCameraAction.SetPermissionGranted -> {
                state.copy(action.isGranted)
            }

        }
    }
}

