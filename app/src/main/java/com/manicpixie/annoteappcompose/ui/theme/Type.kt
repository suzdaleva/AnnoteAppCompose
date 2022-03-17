package com.manicpixie.annoteappcompose.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import com.manicpixie.annoteappcompose.R

val urbanistFont = FontFamily(
    listOf(
        Font(R.font.urbanist_medium, FontWeight.Medium),
        Font(R.font.urbanist_bold, FontWeight.Bold),
        Font(R.font.urbanist_semibold, FontWeight.SemiBold),
        Font(R.font.urbanist_extralight, FontWeight.ExtraLight),
        Font(R.font.urbanist_extrabold, FontWeight.ExtraBold)
    )
)

val Typography = Typography(
    caption = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Bold,
    ),
    body1 = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Bold,
    ),
    body2 = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.Medium,
    ),
    h1 = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.SemiBold,
        color = White,
        letterSpacing = 0.07.em
    ),
    h2 = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.SemiBold,
        color = PrimaryBlack,
        letterSpacing = 0.04.em,
    ),
    h3 = TextStyle(
        fontFamily = urbanistFont,
        fontWeight = FontWeight.SemiBold,
        color = DemiGray,
        letterSpacing = 0.04.em,
    )
)