package com.manicpixie.annoteappcompose.presentation.util

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppButton(
    text: String,
    fontColor: Color,
    backgroundColor: Color,
    width: Dp,
    onClick: () -> Unit
) {
    Button(
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        onClick = onClick, modifier = Modifier
            .width(width)
            .height(42.dp)
    ) {
        Text(
            color = fontColor,
            text = text.uppercase(),
            style = MaterialTheme.typography.h1,
            fontSize = dpToSp(dp = 20.dp)
        )
    }
}