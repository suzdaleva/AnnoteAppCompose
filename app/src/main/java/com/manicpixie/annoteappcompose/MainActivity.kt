package com.manicpixie.annoteappcompose


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.manicpixie.annoteappcompose.presentation.main.MainScreen
import com.manicpixie.annoteappcompose.presentation.onboarding.OnboardingScreen
import com.manicpixie.annoteappcompose.presentation.util.rememberPreference
import com.manicpixie.annoteappcompose.presentation.util.screenHeight
import com.manicpixie.annoteappcompose.ui.theme.AnnoteAppComposeTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(
        ExperimentalAnimationApi::class,
        ExperimentalMaterialApi::class,
        ExperimentalMotionApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        val isShown = intent.getBooleanExtra("isOnboardingScreenShown", false)
        super.onCreate(savedInstanceState)

        setContent {
            AnnoteAppComposeTheme {
                var isOnboardingScreenShown by rememberPreference(
                    booleanPreferencesKey("isOnboardingScreenShown"),
                    false
                )
                val isVisible = remember {
                    mutableStateOf(false)
                }

                if (isShown || isVisible.value) MainScreen() else OnboardingScreen(
                    screenHeight = screenHeight(),
                    onGettingStartedClick = {
                        isOnboardingScreenShown = true
                        isVisible.value = true
                    },
                )
            }
        }
    }
}