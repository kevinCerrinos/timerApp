package com.kev.timerapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.kev.timerapp.presentation.theme.TimerAppTheme

@Composable
fun CircleDot(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary
){
    Box (
        modifier = modifier
            .size(TimerAppTheme.dimens.iconSizeSmall)
            .clip(shape = CircleShape)
            .background(color)
    )
}

@Preview(
    name = "CircleDotPreview",
    showBackground = true
)
@Composable
fun CircleDotPreview(){
    TimerAppTheme {
        CircleDot()
    }
}