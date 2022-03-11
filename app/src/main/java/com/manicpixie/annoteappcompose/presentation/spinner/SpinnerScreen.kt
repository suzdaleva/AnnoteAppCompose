package com.manicpixie.annoteappcompose.presentation.spinner


import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.airbnb.lottie.compose.*
import com.manicpixie.annoteappcompose.R
import com.manicpixie.annoteappcompose.presentation.spinner.components.InfiniteSpinner
import com.manicpixie.annoteappcompose.presentation.util.Constants.months
import com.manicpixie.annoteappcompose.presentation.util.Screen
import com.manicpixie.annoteappcompose.presentation.util.dpToSp
import com.manicpixie.annoteappcompose.ui.theme.PrimaryBlack
import com.manicpixie.annoteappcompose.ui.theme.White
import java.time.Year
import java.util.*


@Composable
fun SpinnerScreen(
    navController: NavController,
    onClick: () -> Unit
) {


    BackHandler {
        onClick()
        navController.navigate(Screen.CalendarScreen.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                inclusive = true
            }
        }
    }


    val chosenYear = remember { mutableStateOf(Calendar.getInstance().get(Calendar.YEAR) * 12) }
    val chosenMonth = remember { mutableStateOf(Calendar.MONTH) }


    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.spinner_anim))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    fun calculatePage(): Int {
        return (chosenYear.value - Calendar.getInstance()
            .get(Calendar.YEAR)) * 12 + chosenMonth.value - Calendar.getInstance()
            .get(Calendar.MONTH)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LottieAnimation(
            modifier = Modifier.padding(horizontal = 15.dp),
            composition = composition,
            progress = progress
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            InfiniteSpinner(
                modifier = Modifier.width(130.dp),
                list = months,
                firstIndex = Int.MAX_VALUE / 2 - 4 + Date().month,
                onSelect = {
                    chosenMonth.value = months.indexOf(it)
                }
            )
            Spacer(modifier = Modifier.width(20.dp))
            InfiniteSpinner(
                modifier = Modifier.width(60.dp),
                list = (1950..2050).map { it.toString() }.toList(),
                firstIndex = (1950..2050).toList().indexOf(Year.now().value - 1),
                onSelect = {
                    chosenYear.value = it.toInt()
                }
            )
        }
        Button(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 30.dp, end = 30.dp)
                .width(108.dp)
                .height(42.dp),
            onClick = {
                onClick()
                navController.navigate(Screen.CalendarScreen.route + "?currentPage=${calculatePage()}") {
                    popUpTo(Screen.CalendarScreen.route) {
                        inclusive = true
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = PrimaryBlack)
        ) {
            Date().month
            Text(
                text = stringResource(id = R.string.go).uppercase(),
                style = MaterialTheme.typography.h1,
                fontSize = dpToSp(dp = 20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                tint = White,
                painter = painterResource(id = R.drawable.button_arrows),
                contentDescription = "Go"
            )
        }
    }

}