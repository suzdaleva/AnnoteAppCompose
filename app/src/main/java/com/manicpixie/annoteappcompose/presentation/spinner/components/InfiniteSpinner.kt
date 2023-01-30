package com.manicpixie.annoteappcompose.presentation.spinner.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.manicpixie.annoteappcompose.presentation.util.dpToSp
import com.manicpixie.annoteappcompose.ui.theme.PrimaryBlack
import com.manicpixie.annoteappcompose.ui.theme.spinnerGradient
import com.manicpixie.annoteappcompose.ui.theme.urbanistFont
import kotlinx.coroutines.launch


@Composable
fun InfiniteSpinner(
    modifier: Modifier = Modifier,
    list: List<String>,
    firstIndex: Int,
    onSelect: (String) -> Unit
) {

    val listState = rememberLazyListState(firstIndex)
    val coroutineScope = rememberCoroutineScope()

    val currentValue = remember {
        mutableStateOf("")
    }

    LaunchedEffect(!listState.isScrollInProgress) {
        coroutineScope.launch {
            onSelect(currentValue.value)
            listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
        }
    }


    Box(
        modifier = Modifier
            .height(106.dp)
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            items(count = Int.MAX_VALUE, itemContent = {
                val index = it % list.size
                if (it == listState.firstVisibleItemIndex + 1) {
                    currentValue.value = list[index]
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    modifier = modifier.alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f),
                    text = list[index].uppercase(),
                    color = PrimaryBlack,
                    style = TextStyle(
                        textAlign = TextAlign.Center,
                        fontFamily = urbanistFont,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.02.em,
                        fontSize = dpToSp(dp = 22.dp)
                    )
                )
                Spacer(modifier = Modifier.height(6.dp))
            })
        }
        Spacer(
            modifier = modifier
                .background(brush = spinnerGradient)
                .height(106.dp)
                .width(120.dp)
        )
    }

}