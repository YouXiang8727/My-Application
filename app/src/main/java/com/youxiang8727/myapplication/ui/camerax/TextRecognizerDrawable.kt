package com.youxiang8727.myapplication.ui.camerax

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import com.elvishew.xlog.XLog
import com.google.mlkit.vision.text.Text

data class TextRecognizerDrawable(
    val text: List<Text.Line>
) : Drawable() {
    private val boundingRectPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.YELLOW
        strokeWidth = 5F
        alpha = 200
    }

    private val contentRectPaint = Paint().apply {
        style = Paint.Style.FILL
        color = Color.YELLOW
        alpha = 255
    }

    private val contentTextPaint = Paint().apply {
        color = Color.DKGRAY
        alpha = 255
        textSize = 36F
    }

    private val contentPadding = 25

    override fun draw(canvas: Canvas) {
        text.forEach { textBlock ->
            textBlock.boundingBox ?: return@forEach
            XLog.tag("David").i("text[${textBlock.text}] " +
                    "confidence[${textBlock.confidence}] " +
                    "boundingBox[${textBlock.boundingBox}] " +
                    "cornerPoints[${textBlock.cornerPoints?.joinToString(", ")}] ")
            canvas.drawRect(textBlock.boundingBox!!, boundingRectPaint)

            canvas.drawText(
                textBlock.text,
                (textBlock.boundingBox!!.left + contentPadding).toFloat(),
                (textBlock.boundingBox!!.bottom + contentPadding*2).toFloat(),
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
}
