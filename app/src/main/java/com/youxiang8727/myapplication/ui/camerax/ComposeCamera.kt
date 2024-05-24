package com.youxiang8727.myapplication.ui.camerax

import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.elvishew.xlog.XLog
import com.youxiang8727.myapplication.mlkit.TextAnalyzer

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
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val executor = ContextCompat.getMainExecutor(context)

    val previewView = remember {
        PreviewView(context)
    }

    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    val cameraProvider = cameraProviderFuture.get()

    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
        .build()

    val resolutionSelector = ResolutionSelector.Builder()
        .setAspectRatioStrategy(AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY)
        .setResolutionStrategy(
            ResolutionStrategy.HIGHEST_AVAILABLE_STRATEGY
        )
        .build()

    val preview = Preview.Builder()
        .setResolutionSelector(resolutionSelector)
        .build()
        .apply {
            this.setSurfaceProvider(
                previewView.surfaceProvider
            )
        }

    val imageAnalyzer = ImageAnalysis.Builder()
        .setResolutionSelector(resolutionSelector)
        .build()
        .apply {
            this.setAnalyzer(executor, TextAnalyzer { result ->
                XLog.d("analysis result: $result")
            })
        }

    AndroidView(
        factory = { ctx ->
            cameraProviderFuture.addListener({
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            }, executor)
            previewView
        })
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