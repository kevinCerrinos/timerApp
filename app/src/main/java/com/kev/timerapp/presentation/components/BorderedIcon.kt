package com.kev.timerapp.presentation.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import com.kev.timerapp.R
import com.kev.timerapp.presentation.theme.TimerAppTheme

@Composable
fun BorderedIcon(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    onTap: () -> Unit = {}
){
    Icon(
        modifier = modifier
            .size(TimerAppTheme.dimens.iconSizeNormal)
            .border(
                width = TimerAppTheme.dimens.borderNormal,
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            )
            .padding(TimerAppTheme.dimens.paddingSmall)
            .clickable { onTap() },
        imageVector = ImageVector.vectorResource(id = icon),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary
    )
}

@Preview(
    name = "BorderedIconPreview",
    showBackground = true
)
@Composable
fun BorderedIconPreview(){
    TimerAppTheme{
        BorderedIcon(icon = R.drawable.ic_menu)
    }
}
