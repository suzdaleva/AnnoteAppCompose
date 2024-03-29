package com.manicpixie.annoteappcompose.presentation.calendar.components

import android.annotation.SuppressLint
import androidx.compose.animation.core.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.manicpixie.annoteappcompose.presentation.calendar.CalendarViewModel
import com.manicpixie.annoteappcompose.presentation.util.Constants.TOP_CARD_INDEX
import com.manicpixie.annoteappcompose.presentation.util.Constants.TOP_Z_INDEX
import com.manicpixie.annoteappcompose.presentation.util.Constants.cardHeight
import com.manicpixie.annoteappcompose.presentation.util.Constants.paddingOffset
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*


private fun calculateScale(idx: Int): Float {
    return when (idx) {
        1 -> 0.97f
        2 -> 0.94f
        else -> 1f
    }
}

private fun calculateOffset(idx: Int): Int {
    return when (idx) {
        1 -> -(paddingOffset * idx * 1.1).toInt()
        2 -> -(paddingOffset * idx * 1.1).toInt()
        else -> -paddingOffset.toInt()
    }
}


@SuppressLint("ModifierFactoryExtensionFunction")
fun makeCardModifier(
    scope: CoroutineScope,
    cardIndex: Int,
    scale: Float,
    zIndex: Float,
    offset: Animatable<Offset, AnimationVector2D>,
    animationSpec: FiniteAnimationSpec<Offset>,
    offsetY: Int,
    rearrangeForward: suspend () -> Unit,
    rearrangeBackward: () -> Unit
): Modifier {


    return if (cardIndex > TOP_CARD_INDEX) Modifier
        .graphicsLayer {
            translationY =
                if (offset.value.y != 0f) {
                    min(
                        abs(offset.value.y),
                        paddingOffset * 1.1f
                    )
                } else 0f
            scaleX = if (offset.value.y != 0f) {
                min(scale + (abs(offset.value.y) / 1000), 1.06f - (cardIndex * 0.03f))
            } else scale
            scaleY = if (offset.value.y != 0f) {
                min(scale + (abs(offset.value.y) / 1000), 1.06f - (cardIndex * 0.03f))
            } else scale
        }
        .scale(scale)
        .offset { IntOffset(0, offsetY) }
        .zIndex(zIndex)
        .fillMaxWidth()
        .height(cardHeight)
    else Modifier
        .scale(scale)
        .offset { IntOffset(offset.value.x.roundToInt(), offset.value.y.roundToInt()) }
        .zIndex(zIndex)
        .fillMaxWidth()
        .height(cardHeight)
        .pointerInput(Unit) {
            detectTapGestures(
                onDoubleTap = {
                    scope.launch {
                        rearrangeBackward()
                        offset.animateTo(
                            targetValue = Offset(-600f, 600f),
                            animationSpec = snap()
                        )
                        offset.animateTo(
                            targetValue = Offset(0f, 0f),
                            animationSpec = tween(
                                durationMillis = 100,
                                easing = LinearEasing
                            )
                        )
                    }
                }
            )

        }
        .pointerInput(Unit) {
            detectDragGestures { change, dragAmount ->
                val dragOffset = Offset(
                    offset.value.x + change.positionChange().x,
                    offset.value.y + change.positionChange().y
                )
                scope.launch {

                    offset.snapTo(dragOffset)


                    if (change.positionChange() != Offset.Zero) change.consume()
                    val x = when {

                        offset.value.x > 250 -> size.width.toFloat()
                        offset.value.x < -250 -> -size.width.toFloat()
                        else -> 0f
                    }
                    val y = when {

                        offset.value.y > 250 -> size.height.toFloat() + 1000
                        offset.value.y < -250 -> -size.height.toFloat() - 1000
                        else -> 0f
                    }

                    offset.animateTo(
                        targetValue = Offset(x, y),
                        animationSpec = animationSpec
                    )
                    if (abs(offset.value.x) == size.width.toFloat() || abs(offset.value.y) == size.height.toFloat() + 1000) {
                        rearrangeForward()
                    }
                }
            }
        }
}


@Composable
fun CardDeck(
    onClick: (Int?, String) -> Unit,
    modifier: Modifier = Modifier,
    dataSource: List<Int> = (0..2399).map { 0 }.toList(),
    calendarViewModel: CalendarViewModel = hiltViewModel()
) {
    val visibleCards: Int = StrictMath.min(3, dataSource.size)

    val scope = rememberCoroutineScope()
    val firstCard = remember { mutableStateOf(calendarViewModel.currentPage.value) }
    val secondCard = remember { mutableStateOf(calendarViewModel.currentPage.value + 1) }

    val offset: Animatable<Offset, AnimationVector2D> = remember {
        Animatable(
            Offset(0f, 0f),
            Offset.VectorConverter,
        )
    }

    val animationSpec: FiniteAnimationSpec<Offset> = tween(
        durationMillis = 100,
        easing = FastOutLinearInEasing
    )


    suspend fun rearrangeForward() {
        if (firstCard.value == dataSource.size - 1) {
            firstCard.value = 0
        } else firstCard.value++
        delay(100)
        secondCard.value = firstCard.value + 1
        offset.animateTo(
            targetValue = Offset(0f, 0f),
            animationSpec = snap()
        )
    }

    fun rearrangeBackward() {

        if (firstCard.value == -(dataSource.size - 1)) {
            firstCard.value = dataSource.size - 1
        } else
            firstCard.value--
        secondCard.value = firstCard.value + 1
    }

    Box(Modifier.fillMaxWidth()) {

        repeat(visibleCards) { index ->
            val zIndex = TOP_Z_INDEX - index
            val scale = calculateScale(index)
            val offsetY = calculateOffset(index)
            val cardModifier =
                makeCardModifier(
                    scope = scope,
                    cardIndex = index,
                    scale = scale,
                    zIndex = zIndex,
                    offsetY = offsetY,
                    offset = offset,
                    rearrangeForward = { rearrangeForward() },
                    rearrangeBackward = { rearrangeBackward() },
                    animationSpec = animationSpec
                )

            CardItem(
                onClick = onClick,
                modifier = cardModifier,
                cardIndex = if (index == 0) firstCard.value else secondCard.value
            )
        }
    }
}



