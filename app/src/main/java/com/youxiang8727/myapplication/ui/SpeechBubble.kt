package com.youxiang8727.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.youxiang8727.myapplication.ui.theme.MyApplicationTheme

@Composable
fun SpeechBubble(
    text: String,
    speaker: Speaker
) {
    MyApplicationTheme {
        Box(
            modifier = Modifier
                .wrapContentSize()
                .defaultMinSize(40.dp)
                .clip(SpeechBubbleShape(speaker = speaker))
                .background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(10.dp, 10.dp)
            )
        }
    }
}