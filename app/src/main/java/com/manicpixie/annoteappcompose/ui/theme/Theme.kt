package com.manicpixie.annoteappcompose.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = PrimaryBlack,
    onPrimary = PrimaryBlack,
    secondary = Gray,
)

private val LightColorPalette = lightColors(
    primary = PrimaryBlack,
    onPrimary = PrimaryBlack,
    secondary = Gray
)

@Composable
fun AnnoteAppComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}