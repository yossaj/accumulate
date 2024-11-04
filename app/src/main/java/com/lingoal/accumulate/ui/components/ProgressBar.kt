package com.lingoal.accumulate.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lingoal.accumulate.ui.dimens.Dimens


@Composable
fun ProgressBar(
    brush: Brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFFF95075),
            Color(0xFFBE6BE5)
        )
    ),
    progress: Float
){
    Box(modifier = Modifier
        .clip(RoundedCornerShape(5.dp))
        .background(Color.Black)
        .height(10.dp)
        .fillMaxWidth()
    ){
        Box(
            modifier = Modifier
                .background(brush)
                .fillMaxHeight()
                .fillMaxWidth(progress)
        )
    }
}

@Preview
@Composable
fun ProgressBarPreview(){
    ProgressBar(progress = 0.5f)
}