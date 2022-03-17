package com.manicpixie.annoteappcompose

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.airbnb.lottie.compose.*
import com.manicpixie.annoteappcompose.presentation.util.rememberPreference
import com.manicpixie.annoteappcompose.ui.theme.AnnoteAppComposeTheme
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var isOnboardingScreenShown: MutableState<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = this
        setContent {
            AnnoteAppComposeTheme {
                isOnboardingScreenShown =
                    rememberPreference(booleanPreferencesKey("isOnboardingScreenShown"), false)

                val compositionResult: LottieCompositionResult =
                    rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
                val progress by animateLottieCompositionAsState(compositionResult.value, speed = 4f)


                LaunchedEffect(true) {
                    compositionResult.await()
                    delay(600)
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.putExtra("isOnboardingScreenShown", isOnboardingScreenShown.value)
                    startActivity(intent)
                    finish()
                }
                Surface(color = MaterialTheme.colors.onPrimary) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LottieAnimation(
                            modifier = Modifier.padding(horizontal = 100.dp),
                            composition = compositionResult.value,
                            progress = progress
                        )
                    }
                }
            }

        }
    }
}