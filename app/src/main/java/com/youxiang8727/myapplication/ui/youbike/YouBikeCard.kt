package com.youxiang8727.myapplication.ui.youbike

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.youxiang8727.myapplication.R
import com.youxiang8727.myapplication.domain.model.YouBikeData

@Composable
fun YouBikeCard(
    youBikeData: YouBikeData
) {
    val context = LocalContext.current
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(4.dp),
        shape = RoundedCornerShape(4.dp),
    ) {
        Column {
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(Color.Black)) {
                        append(youBikeData.sna)
                    }
                    withStyle(SpanStyle(Color.Gray)) {
                        append("(${youBikeData.sno})")
                    }
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(Color.Black)) {
                        append(youBikeData.sarea)
                    }
                    withStyle(SpanStyle(Color.Gray)) {
                        append("(${youBikeData.ar})")
                    }
                },
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = String.format(
                    context.getString(R.string.total_bikes),
                    youBikeData.total
                ),
                maxLines = 1,
            )

            Text(
                text = String.format(
                    context.getString(R.string.available_rent_bikes),
                    youBikeData.available_rent_bikes
                ),
                maxLines = 1,
            )

            Text(
                text = String.format(
                    context.getString(R.string.available_return_bikes),
                    youBikeData.available_return_bikes
                ),
                maxLines = 1,
            )
        }
    }
}