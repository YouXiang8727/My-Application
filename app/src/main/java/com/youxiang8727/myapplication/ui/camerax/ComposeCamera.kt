package com.youxiang8727.myapplication.ui.camerax

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.DialogProperties

@Composable
fun ComposeCamera(
    viewModel: ComposeCameraViewModel
) {
    if (viewModel.state.isPermissionGranted.not()) {
        CameraPermissionDialog(viewModel)
        return
    }

    ComposeCameraView()
}

@Composable
private fun ComposeCameraView() {
    Text(text = "ComposeCameraView")
}

@Composable
private fun CameraPermissionDialog(
    viewModel: ComposeCameraViewModel
) {
    val context = LocalContext.current
    AlertDialog(
        modifier = Modifier,
        onDismissRequest = { },
        confirmButton = {
            Button(
                onClick = { 
                    viewModel.startCameraPermissionActivity(context)
                }
            ) {
                Text(text = "OK")
            }
        }, 
        properties = DialogProperties(
            dismissOnBackPress = false, 
            dismissOnClickOutside = false
        ), 
        title = {
            Text(text = "Alert")
        }, 
        text = {
            Text(text = "Please open camera permission")
        }
    )
}