package com.manicpixie.annoteappcompose.presentation.onboarding


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.airbnb.lottie.compose.*
import com.manicpixie.annoteappcompose.R
import com.manicpixie.annoteappcompose.presentation.util.dpToSp
import com.manicpixie.annoteappcompose.ui.theme.PrimaryBlack
import com.manicpixie.annoteappcompose.ui.theme.White
import com.manicpixie.annoteappcompose.ui.theme.urbanistFont


@Composable
fun FirstOnboardingScreen(
    screenHeight: Dp
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(White)) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.onboarding_animation1))
        val progress by animateLottieCompositionAsState(
            composition,
            iterations = LottieConstants.IterateForever
        )
        LottieAnimation(
            modifier = Modifier
                .padding(start = 7.dp, end = 7.dp, top = 30.dp)
                .align(Alignment.Center),
            composition = composition,
            progress = progress
        )
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
                .align(Alignment.TopStart)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.onboarding_screen1_text1),
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.ExtraLight,
                    color = PrimaryBlack,
                    fontSize = dpToSp(dp = if (screenHeight < 600.dp) 25.dp else 35.dp)
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                modifier = Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.onboarding_screen1_text2),
                style = TextStyle(
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlack,
                    fontSize = dpToSp(dp = if (screenHeight < 600.dp) 25.dp else 35.dp)
                )
            )
        }

        Text(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            text = stringResource(id = R.string.onboarding_screen1_text3).uppercase(),
            style = TextStyle(
                fontFamily = urbanistFont,
                fontWeight = FontWeight.SemiBold,
                color = PrimaryBlack,
                letterSpacing = 0.07.em,
                fontSize = dpToSp(dp = 20.dp)
            )
        )

    }
}


