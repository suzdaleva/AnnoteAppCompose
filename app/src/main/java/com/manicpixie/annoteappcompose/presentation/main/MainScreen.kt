package com.manicpixie.annoteappcompose.presentation.main


import androidx.compose.foundation.layout.height
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.manicpixie.annoteappcompose.presentation.calendar.CalendarScreen
import com.manicpixie.annoteappcompose.presentation.main.components.TopBar
import com.manicpixie.annoteappcompose.presentation.note.NoteScreen
import com.manicpixie.annoteappcompose.presentation.spinner.SpinnerScreen
import com.manicpixie.annoteappcompose.presentation.util.Constants
import com.manicpixie.annoteappcompose.presentation.util.Screen
import com.manicpixie.annoteappcompose.ui.theme.AnnoteAppComposeTheme
import com.manicpixie.annoteappcompose.ui.theme.White


@Composable
fun MainScreen() {

    AnnoteAppComposeTheme {
        Surface {
            val navController = rememberNavController()
            val isPlaying = remember { mutableStateOf(false) }
            val speed = remember { mutableStateOf(0f) }
            Scaffold(
                backgroundColor = White,
                topBar = {
                    TopBar(
                        isPlaying = isPlaying.value,
                        speed = speed.value,
                        modifier = Modifier.height(60.dp),
                        onClick = {
                            when (navController.currentDestination?.route) {
                                Screen.CalendarScreen.route + "?currentPage={currentPage}" -> {
                                    speed.value = Constants.forwardSpeed
                                    isPlaying.value = true
                                    navController.navigate(Screen.SpinnerScreen.route)
                                }
                                Screen.SpinnerScreen.route -> {
                                    speed.value = Constants.backwardSpeed
                                    isPlaying.value = true
                                    navController.navigate(Screen.CalendarScreen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            inclusive = true
                                        }
                                    }
                                }
                                Screen.NoteScreen.route + "?noteId={noteId}&noteDate={noteDate}" -> {
                                    speed.value = Constants.backwardSpeed
                                    isPlaying.value = true
                                    navController.navigate(Screen.CalendarScreen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            inclusive = true
                                        }
                                    }
                                }
                            }

                        })
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
                            navController = navController,
                            onClick = {
                                speed.value = Constants.forwardSpeed
                                isPlaying.value = true
                            })

                    }
                    composable(route = Screen.SpinnerScreen.route) {
                        SpinnerScreen(navController = navController,
                            onClick = {
                                speed.value = Constants.backwardSpeed
                                isPlaying.value = true
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
                            navController = navController,
                            onClick = {
                                speed.value = Constants.backwardSpeed
                                isPlaying.value = true
                            })
                    }
                }
            }
        }
    }
}