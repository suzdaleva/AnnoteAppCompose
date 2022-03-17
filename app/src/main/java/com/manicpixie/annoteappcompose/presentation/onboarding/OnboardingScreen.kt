package com.manicpixie.annoteappcompose.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState


@OptIn(ExperimentalPagerApi::class, androidx.constraintlayout.compose.ExperimentalMotionApi::class)
@Composable
fun OnboardingScreen(
    screenHeight: Dp,
    onGettingStartedClick: () -> Unit
) {
    val pagerState = rememberPagerState(0)

    Box() {
        Column {
            HorizontalPager(
                count = 3,
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) { page ->
                when (page) {
                    0 -> FirstOnboardingScreen(screenHeight = screenHeight)
                    1 -> SecondOnboardingScreen(
                        currentPage = pagerState.currentPage
                    )
                    2 -> ThirdOnboardingScreen(
                        screenHeight = screenHeight,
                        currentPage = pagerState.currentPage,
                        onGettingStartedClick = onGettingStartedClick
                    )
                }
            }
        }
    }

}
