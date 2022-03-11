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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.compose.*
import com.manicpixie.annoteappcompose.ui.theme.AnnoteAppComposeTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnnoteAppComposeTheme {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.splash))
                val progress by animateLottieCompositionAsState(composition, speed = 4f)

                Surface(color = MaterialTheme.colors.onPrimary) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        LottieAnimation(
                            modifier = Modifier.padding(horizontal = 100.dp),
                            composition = composition,
                            progress = progress
                        )
                    }
                }
            }

        }
        val activity = this
        lifecycleScope.launch {
            delay(800)
            startActivity(Intent(activity, MainActivity::class.java))
            finish()
        }

    }
}