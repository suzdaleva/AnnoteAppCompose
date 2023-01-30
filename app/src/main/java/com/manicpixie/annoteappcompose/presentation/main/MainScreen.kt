package com.manicpixie.annoteappcompose.presentation.main


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.manicpixie.annoteappcompose.R
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.airbnb.lottie.compose.*
import com.manicpixie.annoteappcompose.presentation.calendar.CalendarScreen
import com.manicpixie.annoteappcompose.presentation.note.NoteScreen
import com.manicpixie.annoteappcompose.presentation.spinner.SpinnerScreen
import com.manicpixie.annoteappcompose.presentation.util.Constants.backwardSpeed
import com.manicpixie.annoteappcompose.presentation.util.Constants.forwardSpeed
import com.manicpixie.annoteappcompose.presentation.util.Screen
import com.manicpixie.annoteappcompose.presentation.util.noRippleClickable
import com.manicpixie.annoteappcompose.ui.theme.White


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    Surface {

        val navController = rememberNavController()
        val speed = remember { mutableStateOf(0f) }
        val lottieAnimatable = rememberLottieAnimatable()
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.menu_icon))

        LaunchedEffect(speed.value) {
            lottieAnimatable.animate(
                composition = composition,
                speed = speed.value,
            )
        }

        LaunchedEffect(composition) {
            lottieAnimatable.animate(
                composition = composition,
                speed = speed.value,
            )
        }

        Scaffold(
            backgroundColor = White,
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .background(Color.Transparent),
                ) {
                    LottieAnimation(
                        modifier = Modifier
                            .height(72.dp)
                            .width(85.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .padding(top = 2.dp)
                            .noRippleClickable {
                                navigate(navController)
                            },
                        composition = lottieAnimatable.composition,
                        progress = lottieAnimatable.progress
                    )
                }
            }
        ) {
            NavHost(
                navController = navController,
                startDestination = Screen.CalendarScreen.route + "?currentPage={currentPage}"
            ) {
                composable(
                    route = Screen.CalendarScreen.route + "?currentPage={currentPage}",
                    arguments = listOf(
                        navArgument(name = "currentPage") {
                            type = NavType.IntType
                            defaultValue = 0
                        })
                ) {
                    CalendarScreen(
                        onClick = { id, date ->
                            navController.navigate(if (id != null) Screen.NoteScreen.route + "?noteId=${id}&noteDate=${date}" else Screen.NoteScreen.route + "?noteDate=${date}") {
                                popUpTo(Screen.CalendarScreen.route) {
                                    inclusive = true
                                }
                            }
                        })

                }
                composable(route = Screen.SpinnerScreen.route) {
                    SpinnerScreen(
                        onClick = {
                            navController.navigate(Screen.CalendarScreen.route + "?currentPage=$it") {
                                popUpTo(Screen.CalendarScreen.route) {
                                    inclusive = true
                                }
                            }
                        })
                }
                composable(
                    route = Screen.NoteScreen.route + "?noteId={noteId}&noteDate={noteDate}",
                    arguments = listOf(
                        navArgument(name = "noteId") {
                            type = NavType.IntType
                            defaultValue = -1
                        },
                        navArgument(name = "noteDate") {
                            type = NavType.StringType
                            defaultValue = ""
                        })
                ) {
                    val date = it.arguments?.getString("noteDate") ?: ""
                    NoteScreen(
                        date = date,
                        onClick = { currentPage ->
                            navController.navigate(Screen.CalendarScreen.route + "?currentPage=${currentPage}") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        })
                }
            }
            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                when (destination.route) {
                    Screen.CalendarScreen.route + "?currentPage={currentPage}" -> {
                        speed.value = backwardSpeed
                    }
                    else -> {
                        speed.value = forwardSpeed
                    }
                }
            }
        }
    }
}


fun navigate(navController: NavController) {
    when (navController.currentDestination?.route) {
        Screen.CalendarScreen.route + "?currentPage={currentPage}" -> {
            navController.navigate(Screen.SpinnerScreen.route)
        }
        Screen.NoteScreen.route + "?noteId={noteId}&noteDate={noteDate}" -> {
            navController.navigate(Screen.CalendarScreen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        }
        Screen.SpinnerScreen.route -> {
            navController.navigate(Screen.CalendarScreen.route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        }
    }
}


