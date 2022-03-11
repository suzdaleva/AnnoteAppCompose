package com.manicpixie.annoteappcompose.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


val PrimaryBlack = Color(0xFF100208)
val Gray = Color(0x33100208)
val White = Color(0xFFFFFFFF)
val DemiGray = Color(0xFF6B6B6B)

val spinnerGradient = Brush.linearGradient(
    0.0f to Color(0xFFFFFFFF),
    0.3f to Color(0x00000000),
    0.5f to Color(0x00000000),
    0.7f to Color(0x00000000),
    1.0f to Color(0xFFFFFFFF),
    start = Offset(0.0f, -13.0f),
    end = Offset(0.0f, 320.0f)
)