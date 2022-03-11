package com.manicpixie.annoteappcompose.presentation.calendar

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.navigation.NavController
import com.manicpixie.annoteappcompose.R
import com.manicpixie.annoteappcompose.presentation.calendar.components.CardDeck
import com.manicpixie.annoteappcompose.presentation.util.dpToSp


@OptIn(ExperimentalMotionApi::class)
@Composable
fun CalendarScreen(
    onClick: () -> Unit,
    navController: NavController,
) {
    val activity = (LocalContext.current as? Activity)

    BackHandler {
        activity?.finish()
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(bottom = 60.dp), contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(id = R.string.calendar_title),
                style = MaterialTheme.typography.caption,
                fontSize = dpToSp(dp = 40.dp),
                textAlign = TextAlign.Center,
            )
        }
        CardDeck(
            onClick = onClick,
            modifier = Modifier.weight(1f),
            navController = navController
        )
    }
}

