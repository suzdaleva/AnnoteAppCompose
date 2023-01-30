package com.manicpixie.annoteappcompose.presentation.note


import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.manicpixie.annoteappcompose.R
import com.manicpixie.annoteappcompose.presentation.util.AppButton
import com.manicpixie.annoteappcompose.presentation.util.dpToSp
import com.manicpixie.annoteappcompose.ui.theme.PrimaryBlack
import com.manicpixie.annoteappcompose.ui.theme.White
import com.manicpixie.annoteappcompose.ui.theme.urbanistFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt


enum class TextState {
    Focused,
    Unfocused
}


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun NoteScreen(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onClick: (Int) -> Unit,
    date: String,
    viewModel: NoteViewModel = hiltViewModel()
) {

    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }

    val contentState = viewModel.noteContent.value
    val scope = rememberCoroutineScope()
    val lineCount = remember { mutableStateOf(0) }
    val overflowHeight = remember { mutableStateOf(true) }
    val dragDirection = remember { mutableStateOf(0) }
    var currentState by remember { mutableStateOf(TextState.Unfocused) }
    val transition = updateTransition(currentState, "position")



    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is NoteViewModel.UiEvent.ShowSnackBar -> {
                    snackbarHostState.value.showSnackbar(
                        message =
                        event.message,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        }
    }



    DisposableEffect(lifecycleOwner) {

        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                if (!(viewModel.isSaveButtonPressed.value || viewModel.isDeleteButtonPressed.value))
                    viewModel.onBackPressed()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val height = LocalConfiguration.current.screenHeightDp.dp

    val lineCountAvailableInt = ((height.value - 205) / 49).roundToInt()

    val buttonsOffset by transition.animateDp(
        transitionSpec = {
            tween(
                durationMillis = 700
            )
        },
        label = "buttons offset"
    ) { state ->
        when (state) {
            TextState.Unfocused -> 0.dp
            TextState.Focused -> 100.dp
        }
    }


    val connection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y < 0) {
                    dragDirection.value = 1
                } else {
                    dragDirection.value = 0
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                return Velocity.Zero
            }
        }
    }


    fun navigateBack() {
        val currentPage = (viewModel.currentDate.value.get(Calendar.YEAR) - Calendar.getInstance()
            .get(Calendar.YEAR)) * 12 + viewModel.currentDate.value.get(Calendar.MONTH) - Calendar.getInstance()
            .get(Calendar.MONTH)
        onClick(currentPage)
    }



    BackHandler {
        navigateBack()
    }



    Scaffold(
        snackbarHost = {
            SnackbarHost(
                modifier = Modifier,
                hostState = snackbarHostState.value,
                snackbar = { snackbarData: SnackbarData ->
                    Card(
                        backgroundColor = PrimaryBlack,
                        shape = RoundedCornerShape(9.dp),
                        modifier = Modifier
                            .height(100.dp)
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(start = 16.dp, top = 14.dp, bottom = 14.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = snackbarData.message,
                                color = Color.White,
                                style = TextStyle(
                                    fontFamily = urbanistFont,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = dpToSp(dp = 15.dp),
                                    lineHeight = dpToSp(dp = 18.dp)
                                )
                            )
                        }
                    }
                }
            )
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(horizontal = 30.dp)
                .nestedScroll(connection)
        ) {

            BasicTextField(
                decorationBox = { innerTextField ->
                    Box {
                        if (contentState.isEmpty()) {
                            Text(
                                color = Color.LightGray,
                                text = "You can start typing here...",
                                style = MaterialTheme.typography.h2.copy(
                                    fontSize = dpToSp(dp = 20.dp),
                                    lineHeight = dpToSp(dp = 49.dp),
                                )
                            )
                        }
                        innerTextField()
                    }
                },
                onTextLayout = {
                    lineCount.value = it.lineCount
                    overflowHeight.value = it.hasVisualOverflow
                },
                modifier = Modifier
                    .fillMaxSize()
                    .focusTarget()
                    .onFocusEvent {
                        if (it.hasFocus) {
                            currentState = TextState.Focused
                            scope.launch {
                                delay(300)
                            }
                        } else {
                            currentState = TextState.Unfocused
                        }
                    }
                    .padding(
                        top = if (dragDirection.value == 0
                            || lineCount.value <= lineCountAvailableInt
                        ) 70.dp else 30.dp
                    ),
                maxLines = lineCountAvailableInt,
                singleLine = false,
                value = contentState,
                onValueChange = {
                    viewModel.onEvent(NoteEvent.EnteredContent(it))
                },
                textStyle = MaterialTheme.typography.h2.copy(
                    fontSize = dpToSp(dp = 20.dp),
                    lineHeight = dpToSp(dp = 49.dp)
                ),
            )
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth(),
                visible = dragDirection.value == 0 || lineCount.value <= lineCountAvailableInt,
                enter = slideInVertically(
                    initialOffsetY = { -400 },
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = LinearEasing
                    )
                ) + fadeIn(
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = LinearOutSlowInEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { -400 },
                    animationSpec = tween(
                        durationMillis = 100,
                        easing = LinearEasing
                    )
                ) + fadeOut(
                    animationSpec = tween(
                        durationMillis = 50
                    )
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White),
                    text = date,
                    color = PrimaryBlack,
                    style = MaterialTheme.typography.caption,
                    fontSize = dpToSp(dp = 40.dp),
                    textAlign = TextAlign.Center
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 30.dp),
                visible = dragDirection.value == 0 || lineCount.value <= lineCountAvailableInt,
                enter = slideInVertically(
                    initialOffsetY = { 200 },
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { 200 },
                    animationSpec = tween(
                        durationMillis = 200,
                        easing = LinearEasing
                    )
                )
            ) {

                Row(
                    modifier = Modifier.offset(0.dp, buttonsOffset),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    AppButton(
                        modifier = Modifier.width(115.dp),
                        text = stringResource(id = R.string.delete_button),
                        fontColor = PrimaryBlack,
                        backgroundColor = White,
                        onClick = {
                            viewModel.onEvent(NoteEvent.DeleteNote)
                            navigateBack()
                        }
                    )
                    AppButton(
                        modifier = Modifier.width(108.dp),
                        text = stringResource(id = R.string.save_button),
                        fontColor = White,
                        backgroundColor = PrimaryBlack,
                        onClick = {
                            viewModel.onEvent(NoteEvent.SaveNote)
                            if (contentState.isNotEmpty()) {
                                navigateBack()
                            }
                        }
                    )
                }
            }
        }
    }
}
