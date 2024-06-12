package com.youxiang8727.myapplication.ui

import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

data class SpeechBubbleShape(
    private val cornerRadius: Dp = 5.dp,
    private val tipSize: Dp = 5.dp,
    private val speaker: Speaker = Speaker.OTHER
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val cornerRadius = with(density) { cornerRadius.toPx() }
        val tipSize = with(density) { tipSize.toPx() }

        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    left = tipSize,
                    top = 0f + tipSize,
                    right = size.width - tipSize,
                    bottom = size.height - tipSize,
                    radiusX = cornerRadius,
                    radiusY = cornerRadius
                )
            )

            val tipAngle = TipAngle(size, tipSize, cornerRadius, speaker)

            moveTo(tipAngle.topX, tipAngle.topY)

            lineTo(tipAngle.leftX, tipAngle.leftY)

            lineTo(tipAngle.rightX, tipAngle.rightY)

            close()
        }

        return Outline.Generic(path)
    }
}

enum class Speaker {
    ME, OTHER
}

class TipAngle private constructor(
    val topX: Float,
    val topY: Float,
    val leftX: Float,
    val leftY: Float,
    val rightX: Float,
    val rightY: Float
) {
    companion object {
        operator fun invoke(
            size: Size,
            tipSize: Float,
            cornerRadius: Float,
            speaker: Speaker
        ): TipAngle {
            return when (speaker) {
                Speaker.OTHER -> {
                    TipAngle(
                        tipSize,
                        size.height - tipSize - cornerRadius,
                        0f,
                        size.height,
                        tipSize + cornerRadius,
                        size.height - tipSize
                    )
                }

                Speaker.ME -> {
                    TipAngle(
                        size.width - tipSize,
                        size.height - tipSize - cornerRadius,
                        size.width - tipSize - cornerRadius,
                        size.height - tipSize,
                        size.width,
                        size.height
                    )
                }
            }
        }
    }
}
