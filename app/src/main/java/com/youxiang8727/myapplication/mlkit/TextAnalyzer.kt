package com.youxiang8727.myapplication.mlkit

import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.elvishew.xlog.XLog
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextAnalyzer(
    private val onDetectTextUpdated: (List<Text.Line>) -> Unit
): ImageAnalysis.Analyzer {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val textRecognizer = TextRecognition.getClient(
        TextRecognizerOptions.DEFAULT_OPTIONS
    )

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage = imageProxy.image ?: return@launch
            val inputImage = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            suspendCoroutine { continuation ->
                textRecognizer.process(inputImage)
                    .addOnSuccessListener { task ->
                        onDetectTextUpdated(task.textBlocks.flatMap { it.lines })
                    }
                    .addOnCanceledListener {
                        XLog.e("recognize text canceled")
                    }
                    .addOnFailureListener { exception ->
                        XLog.e("recognize text failed, exception: $exception")
                    }
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                        imageProxy.close()
                    }
            }
        }
    }
}