package com.youxiang8727.myapplication.ui.camerax

import android.content.Context
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.youxiang8727.myapplication.MviViewModel

class ComposeCameraViewModel(

): MviViewModel<ComposeCameraState, ComposeCameraAction>(
    reducer = ComposeCameraActionReducer(),
    initialState = ComposeCameraState()
) {
    fun setCameraPermissionGranted(
        isGranted: Boolean
    ) {
        dispatch(
            ComposeCameraAction.SetPermissionGranted(isGranted)
        )
    }

    fun startCameraPermissionActivity(
        context: Context
    ) {
        XXPermissions.startPermissionActivity(context, Permission.CAMERA)
    }

}