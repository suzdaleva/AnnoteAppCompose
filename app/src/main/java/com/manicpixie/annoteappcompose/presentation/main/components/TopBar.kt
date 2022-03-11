package com.manicpixie.annoteappcompose.presentation.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.manicpixie.annoteappcompose.R

@Composable
fun TopBar(
    onClick: ()-> Unit,
    isPlaying: Boolean,
    speed: Float,
    modifier : Modifier = Modifier
){
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.menu_icon))
    val progress by animateLottieCompositionAsState(composition, isPlaying = isPlaying, speed = speed)
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp)
        .background(Color.Transparent),
    ){
        LottieAnimation(
            modifier = Modifier
                .height(70.dp)
                .width(80.dp)
                .clip(RoundedCornerShape(8.dp))
                .padding(top = 2.dp)
                .noRippleClickable { onClick() },
            composition = composition,
            progress = progress)
    }
}

inline fun Modifier.noRippleClickable(crossinline onClick: ()->Unit): Modifier = composed {
    clickable(indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}