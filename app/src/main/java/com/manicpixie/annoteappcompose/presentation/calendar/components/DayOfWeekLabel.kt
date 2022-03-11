package com.manicpixie.annoteappcompose.presentation.calendar.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.manicpixie.annoteappcompose.presentation.util.dpToSp


@Composable
fun DayOfWeekLabel(
    text: String
) {
    Text(
        style = MaterialTheme.typography.h3,
        fontSize = dpToSp(dp = 18.dp),
        modifier = Modifier.size(45.dp),
        text = text,
        textAlign = TextAlign.Center,
    )
}