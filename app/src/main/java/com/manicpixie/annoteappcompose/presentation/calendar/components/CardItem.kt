package com.manicpixie.annoteappcompose.presentation.calendar.components

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import com.manicpixie.annoteappcompose.R
import com.manicpixie.annoteappcompose.presentation.calendar.CalendarViewModel
import com.manicpixie.annoteappcompose.presentation.main.components.noRippleClickable
import com.manicpixie.annoteappcompose.presentation.util.*
import com.manicpixie.annoteappcompose.presentation.util.Constants.cornerRadiusBig
import com.manicpixie.annoteappcompose.presentation.util.Constants.months
import com.manicpixie.annoteappcompose.presentation.util.Constants.normalElevation
import com.manicpixie.annoteappcompose.ui.theme.PrimaryBlack
import com.manicpixie.annoteappcompose.ui.theme.White
import com.manicpixie.annoteappcompose.ui.theme.urbanistFont
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


@SuppressLint("SimpleDateFormat", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalFoundationApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun CardItem(
    onClick: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier,
    cardIndex: Int,
    viewModel: CalendarViewModel = hiltViewModel()
) {


    val data = mutableListOf<Date>()

    val calendar = (firstPageCalendarDate.clone() as Calendar).apply {
        add(Calendar.MONTH, cardIndex)

        set(Calendar.DAY_OF_MONTH, 1)
    }.setMidnight()

    val scope = rememberCoroutineScope()

    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val firstDayOfWeek = firstPageCalendarDate.firstDayOfWeek
    val monthBeginningCell =
        (if (dayOfWeek < firstDayOfWeek) 7 else 0) + dayOfWeek - firstDayOfWeek
    calendar.add(Calendar.DAY_OF_MONTH, -monthBeginningCell)


    while (data.size < 42) {
        data.add(calendar.time)
        calendar.add(Calendar.DAY_OF_MONTH, 1)
    }


    val pageMonth =
        if ((calendar.get(Calendar.MONTH) - 1) < 0) 11 else calendar.get(Calendar.MONTH) - 1


    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadiusBig),
        elevation = normalElevation
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, start = 15.dp, end = 15.dp)
        ) {

            LazyVerticalGrid(
                userScrollEnabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White),
                cells = GridCells.Fixed(7),
                contentPadding = PaddingValues(horizontal = 2.dp, vertical = 5.dp)
            ) {
                item {
                    DayOfWeekLabel(text = stringResource(id = R.string.first_day))
                }
                item {
                    DayOfWeekLabel(text = stringResource(id = R.string.second_day))
                }
                item {
                    DayOfWeekLabel(text = stringResource(id = R.string.third_day))
                }
                item {
                    DayOfWeekLabel(text = stringResource(id = R.string.fourth_day))
                }
                item {
                    DayOfWeekLabel(text = stringResource(id = R.string.fifth_day))
                }
                item {
                    DayOfWeekLabel(text = stringResource(id = R.string.sixth_day))
                }
                item {
                    DayOfWeekLabel(text = stringResource(id = R.string.seventh_day))
                }
                items(data) { item ->
                    val day = GregorianCalendar().apply {
                        time = item
                    }
                    day.setMidnight()
                    val date = SimpleDateFormat("dd.MM.yyyy").format(day.time)
                    val isTodayMonth = day.get(Calendar.MONTH) == pageMonth

                    var textColor = PrimaryBlack

                    var animatedTextColorState by remember { mutableStateOf(false) }
                    val startColor = Color.Transparent
                    val endColor = White
                    val animatedTextColor by animateColorAsState(
                        if (animatedTextColorState) endColor else startColor,
                        tween(durationMillis = 1000)
                    )

                    val recentlyVisited =
                        viewModel.noteList.value.notes.filter { it.modify_date.timeInMillis > Calendar.getInstance().timeInMillis - 1000 } ==
                                viewModel.noteList.value.notes.filter { it.date == day }
                                && !viewModel.noteList.value.notes.none { it.date == day }
                    Box(
                        modifier = if (isTodayMonth) Modifier
                            .noRippleClickable {
                                scope.launch {
                                    if (viewModel.checkNote(day)) {
                                        val note = viewModel.getNoteByDate(day)
                                        delay(150)
                                        onClick()
                                        if (note != null) navController.navigate(Screen.NoteScreen.route + "?noteId=${note.id}&noteDate=${date}")
                                    } else {
                                        onClick()
                                        navController.navigate(Screen.NoteScreen.route + "?noteDate=${date}")
                                    }
                                }
                            }
                            .size(45.dp) else Modifier.size(45.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isTodayMonth) {
                            if (recentlyVisited) {
                                 val composition by rememberLottieComposition(
                                    LottieCompositionSpec.RawRes(
                                        R.raw.checked
                                    )
                                )
                                val progress by animateLottieCompositionAsState(
                                    clipSpec = LottieClipSpec.Progress(0.1f, 0.95f),
                                    composition = composition,
                                    speed = 1.5f
                                )
                                LottieAnimation(
                                    modifier = Modifier.size(43.dp),
                                    composition = composition,
                                    progress = progress
                                )
                                animatedTextColorState = true
                            } else if (!viewModel.noteList.value.notes.none { it.date == day }) {
                                textColor = White
                                Image(
                                    modifier = Modifier.size(43.dp),
                                    painter = painterResource(id = R.drawable.checked_circle),
                                    contentDescription = "Has note"
                                )
                            }
                            Text(
                                color = if (recentlyVisited) animatedTextColor else textColor,
                                text = day[Calendar.DAY_OF_MONTH].toString(),
                                fontSize = dpToSp(dp = 18.dp),
                                textAlign = TextAlign.Center,
                            )
                            if (day.isToday) {
                                Image(
                                    modifier = Modifier.size(43.dp),
                                    painter = painterResource(id = R.drawable.today_circle),
                                    contentDescription = "Today"
                                )
                            }
                        }
                    }

                }

            }
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = months[pageMonth].uppercase(),
                    fontSize = dpToSp(dp = 20.dp),
                    style = TextStyle(
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryBlack
                    )
                )
                Text(
                    modifier = Modifier,
                    text = if (pageMonth == 11) (calendar.get(Calendar.YEAR) - 1).toString() else (calendar.get(
                        Calendar.YEAR
                    )).toString(),
                    fontSize = dpToSp(dp = 20.dp),
                    style = TextStyle(
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Medium,
                        color = PrimaryBlack
                    )
                )
            }
        }

    }
}


