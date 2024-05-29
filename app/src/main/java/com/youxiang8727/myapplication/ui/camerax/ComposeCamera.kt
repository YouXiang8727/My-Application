package com.youxiang8727.myapplication.ui.camerax

import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.core.util.toRange
import com.youxiang8727.myapplication.mlkit.TextAnalyzer

@Composable
fun ComposeCamera(
    viewModel: ComposeCameraViewModel
) {
    if (viewModel.state.isPermissionGranted.not()) {
        CameraPermissionDialog(viewModel)
        return
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ComposeCameraView(viewModel)
        AnalysisResultDrawableTypeSelector(viewModel)
    }
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
private fun ComposeCameraView(
    viewModel: ComposeCameraViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val executor = ContextCompat.getMainExecutor(context)

    val previewView = remember {
        PreviewView(context).apply {
            this.scaleType = PreviewView.ScaleType.FIT_CENTER
        }
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
            this.setAnalyzer(executor, TextAnalyzer { result, width, height, rotationDegrees ->
                previewView.overlay.clear()
                previewView.overlay.add(
                    AnalysisResultDrawable(
                        result,
                        viewModel.state.analysisResultDrawableType,
                        viewModel.state.analysisResultConfidenceRange,
                        width,
                        height,
                        rotationDegrees
                    )
                )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnalysisResultDrawableTypeSelector(
    viewModel: ComposeCameraViewModel
) {
    val items = listOf(
        AnalysisResultDrawableType.None,
        AnalysisResultDrawableType.Block,
        AnalysisResultDrawableType.Line
    )

    var valueRange by remember {
        mutableStateOf(0f..1f)
    }

    Column {
        if (viewModel.state.analysisResultDrawableType == AnalysisResultDrawableType.Line) {
            Text(
                text = "Confidence[$valueRange]",
                color = Color.Green
            )
            RangeSlider(
                valueRange = 0f..1f,
                value = valueRange,
                onValueChange = { range ->
                    valueRange = range
                },
                onValueChangeFinished = {
                    viewModel.setAnalysisResultConfidenceRange(
                        valueRange.toRange()
                    )
                }
            )
        }
        TabRow(
            selectedTabIndex = items.indexOf(viewModel.state.analysisResultDrawableType),
            modifier = Modifier,
            containerColor = Color.Transparent,
            contentColor = Color.White,
            indicator = {}
        ) {
            items.forEachIndexed { index, textRecognizerDrawableType ->
                Tab(
                    selected = viewModel.state.analysisResultDrawableType == textRecognizerDrawableType,
                    onClick = {
                        viewModel.setAnalysisResultDrawableType(textRecognizerDrawableType)
                    },
                    text = {
                        Text(
                            text = textRecognizerDrawableType::class.java.simpleName,
                            color = if (viewModel.state.analysisResultDrawableType == textRecognizerDrawableType) {
                                Color.Green
                            } else {
                                Color.White
                            }
                        )
                    }
                )
            }
        }
    }
}