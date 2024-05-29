package com.youxiang8727.myapplication.ui.camerax

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.toArgb
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.Line
import com.google.mlkit.vision.text.Text.TextBlock

data class AnalysisResultDrawable(
    val text: Text,
    val state: ComposeCameraState,
    val imageWidth: Int,
    val imageHeight: Int,
    val rotationDegrees: Int
) : Drawable() {
    private val boundingRectPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = state.analysisResultDrawableColor.toArgb()
        strokeWidth = 5F
        alpha = 200
    }

    private val contentRectPaint = Paint().apply {
        style = Paint.Style.FILL
        color = state.analysisResultDrawableColor.toArgb()
        alpha = 255
    }

    private val contentTextPaint = Paint().apply {
        color = state.analysisResultDrawableColor.toArgb()
        alpha = 255
        textSize = 36F
    }

    private val contentPadding = 25

    override fun draw(canvas: Canvas) {
        val blocks: List<DrawableData> = when (state.analysisResultDrawableType) {
            AnalysisResultDrawableType.None -> return
            AnalysisResultDrawableType.Block -> {
                text.textBlocks.map {
                    DrawableData(it)
                }
            }
            AnalysisResultDrawableType.Line -> {
                text.textBlocks.flatMap {
                    it.lines
                }.filter {
                    it.confidence >= state.analysisResultConfidenceRange.lower &&
                            it.confidence <= state.analysisResultConfidenceRange.upper
                }.map {
                    DrawableData(it)
                }
            }
        }

        //畫面縮放比例
        val ratio: Double = when (rotationDegrees) {
            0, 180 -> {
                canvas.width.toDouble() / imageWidth.toDouble()
            }

            90, 270 -> {
                canvas.width.toDouble() / imageHeight.toDouble()
            }

            else -> throw IllegalArgumentException("Invalid rotationDegrees: $rotationDegrees")
        }

        val topStart: Int = when (rotationDegrees) {
            0,180 -> {
                canvas.height - (imageHeight * ratio).toInt()
            }

            90, 270 -> {
                canvas.height - (imageWidth * ratio).toInt()
            }

            else -> throw IllegalArgumentException("Invalid rotationDegrees: $rotationDegrees")
        } / 2

        val scaledImageWidth = (imageWidth * ratio).toInt()
        val scaledImageHeight = (imageHeight * ratio).toInt()

        blocks.forEach { textBlock ->
            val cornerPoints = textBlock.cornerPoints
            if (cornerPoints == null || cornerPoints.size != 4) return@forEach
            // 將座標和尺寸換算回 previewView 的尺寸
            val left = (cornerPoints[0].x * ratio).toInt()
            val top = (cornerPoints[1].y * ratio).toInt()
            val right = (cornerPoints[2].x * ratio).toInt()
            val bottom = (cornerPoints[3].y * ratio).toInt()
            val scaledAndRotatedBoundingBox: Rect = when (rotationDegrees) {
                0 -> {
                    Rect(
                        left,
                        top,
                        right,
                        bottom,
                    )
                }
                90 -> {
                    Rect(
                        scaledImageHeight - bottom,
                        topStart + left,
                        scaledImageHeight - top,
                        topStart + right,
                    )
                }
                180 -> {
                    Rect(
                        scaledImageWidth - right,
                        scaledImageHeight - bottom,
                        scaledImageWidth - left,
                        scaledImageHeight - top,
                    )
                }
               270 -> {
                   Rect(
                       top,
                       scaledImageWidth - right,
                       scaledImageHeight - bottom,
                       scaledImageWidth - left,
                   )
                }
                else -> throw IllegalArgumentException("Invalid rotation degrees: $rotationDegrees")
            }

            canvas.drawRect(
                scaledAndRotatedBoundingBox.left.toFloat(),
                scaledAndRotatedBoundingBox.top.toFloat(),
                scaledAndRotatedBoundingBox.right.toFloat(),
                scaledAndRotatedBoundingBox.bottom.toFloat(),
                boundingRectPaint
            )

            canvas.drawText(
                textBlock.text,
                (scaledAndRotatedBoundingBox.left + contentPadding).toFloat(),
                (scaledAndRotatedBoundingBox.bottom + contentPadding * 2).toFloat(),
                contentTextPaint
            )
        }
    }

    override fun setAlpha(alpha: Int) {
        boundingRectPaint.alpha = alpha
        contentRectPaint.alpha = alpha
        contentTextPaint.alpha = alpha
    }

    override fun setColorFilter(colorFiter: ColorFilter?) {
        boundingRectPaint.colorFilter = colorFilter
        contentRectPaint.colorFilter = colorFilter
        contentTextPaint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    private inner class DrawableData private constructor(
        val text: String,
        val cornerPoints: Array<Point>?
    ) {
        constructor(block: TextBlock): this(
            block.text,
            block.cornerPoints
        )

        constructor(line: Line): this(
            line.text,
            line.cornerPoints
        )
    }
}
