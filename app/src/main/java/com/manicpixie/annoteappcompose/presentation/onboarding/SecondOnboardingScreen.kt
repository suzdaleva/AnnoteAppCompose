package com.manicpixie.annoteappcompose.presentation.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.manicpixie.annoteappcompose.R
import com.manicpixie.annoteappcompose.presentation.util.dpToSp
import com.manicpixie.annoteappcompose.ui.theme.PrimaryBlack
import com.manicpixie.annoteappcompose.ui.theme.White
import com.manicpixie.annoteappcompose.ui.theme.urbanistFont


@Composable
fun SecondOnboardingScreen(
    currentPage: Int
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(White)) {
        val lottieAnimatable = rememberLottieAnimatable()
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.onboarding_animation2))

        LaunchedEffect(currentPage) {
            lottieAnimatable.animate(
                composition = composition,
                iterations = LottieConstants.IterateForever

            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                modifier = Modifier
                    .height(370.dp),
                composition = lottieAnimatable.composition,
                progress = lottieAnimatable.progress
            )
            Text(
                modifier = Modifier.padding(top = 30.dp),
                text = stringResource(id = R.string.onboarding_screen2_text1),
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryBlack,
                    fontSize = dpToSp(26.dp),
                    textAlign = TextAlign.Center
                )
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = stringResource(id = R.string.onboarding_screen2_text2),
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryBlack,
                    fontSize = dpToSp(18.dp),
                    textAlign = TextAlign.Center
                )
            )
        }
    }
}