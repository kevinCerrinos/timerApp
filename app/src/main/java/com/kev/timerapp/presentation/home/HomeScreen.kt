package com.kev.timerapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kev.timerapp.R
import com.kev.timerapp.domain.model.TimerTypeEnum
import com.kev.timerapp.presentation.components.AutoResizedText
import com.kev.timerapp.presentation.components.BorderedIcon
import com.kev.timerapp.presentation.components.CircleDot
import com.kev.timerapp.presentation.components.CustomButton
import com.kev.timerapp.presentation.components.InformationItem
import com.kev.timerapp.presentation.components.TimerTypeItem
import com.kev.timerapp.presentation.theme.TimerAppTheme


@Composable
fun HomeScreen(homeViewModel: HomeScreenViewModel = hiltViewModel()){

    val timerValueState by remember { mutableStateOf(homeViewModel.timerValueState) }
    val timerTypeState by remember { mutableStateOf(homeViewModel.timerTypeState) }
    val roundsState by remember { mutableStateOf(homeViewModel.roundsState) }
    val todayTimeState by remember { mutableStateOf(homeViewModel.todayTimeState) }

    val lifecycleOwner = LocalLifecycleOwner.current
    
    LaunchedEffect(homeViewModel) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
            homeViewModel.getTimerSessionByDate()
        }
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(TimerAppTheme.dimens.paddingMedium)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box (
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ){
            Icon(
                modifier = Modifier
                    .size(TimerAppTheme.dimens.iconSizeNormal),
                painter = painterResource(id = R.drawable.ic_menu),
                contentDescription = "Menu",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        AutoResizedText(
            text = "Focus Timer",
            textStyle = MaterialTheme.typography.displayMedium.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(TimerAppTheme.dimens.spacerMedium))
        Row {
            CircleDot()
            Spacer(modifier = Modifier.width(TimerAppTheme.dimens.spacerNormal))
            CircleDot()
            Spacer(modifier = Modifier.width(TimerAppTheme.dimens.spacerNormal))
            CircleDot(
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(modifier = Modifier.width(TimerAppTheme.dimens.spacerNormal))
            CircleDot(
                color = MaterialTheme.colorScheme.tertiary
            )
        }
        Spacer(modifier = Modifier.height(TimerAppTheme.dimens.spacerMedium))
        TimerSession(
            timer = homeViewModel.millisToMinutes(timerValueState.longValue),
            onDecreasedTap = {
                homeViewModel.onDecreaseTime()
            },
            onIncreasedTap = {
                homeViewModel.onIncreaseTime()
            }
        )
        Spacer(modifier = Modifier.height(TimerAppTheme.dimens.spacerMedium))
        TimerTypeSession(
            type = timerTypeState.value,
            onTap = {
                type -> homeViewModel.onUpdateType(type)
            }
        )
        Spacer(modifier = Modifier.height(TimerAppTheme.dimens.spacerMedium))
        Column (
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            CustomButton(
                text = "Start",
                textColor = MaterialTheme.colorScheme.surface,
                buttonColor = MaterialTheme.colorScheme.primary,
                onTap = {
                    homeViewModel.onStartTimer()
                }
            )
            CustomButton(
                text = "Reset",
                textColor = MaterialTheme.colorScheme.primary,
                buttonColor = MaterialTheme.colorScheme.surface,
                onTap = {
                    homeViewModel.onCancelTimer(true)
                }
            )
        }
        Spacer(modifier = Modifier.height(TimerAppTheme.dimens.spacerMedium))
        InformationSession(
            modifier = Modifier.weight(1f),
            round = roundsState.intValue.toString(),
            time = homeViewModel.millisToHours(todayTimeState.longValue)
        )
    }
}


@Composable
fun InformationSession(
    modifier: Modifier = Modifier,
    round: String,
    time: String
){
    Box (
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ){
        Row (
            modifier = Modifier.align(Alignment.BottomCenter)
        ){
            InformationItem(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = round,
                label = "rounds"
            )
            Spacer(modifier = modifier
                .fillMaxWidth()
                .weight(1f))
            InformationItem(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = time,
                label = "rounds"
            )
        }
    }
}


@Composable
fun TimerSession(
    modifier: Modifier = Modifier,
    timer: String,
    onIncreasedTap: () -> Unit = {},
    onDecreasedTap: () -> Unit = {}
){
    Row (
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ){
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            BorderedIcon(icon = R.drawable.ic_minus, onTap = onDecreasedTap)
            Spacer(modifier = Modifier.height(TimerAppTheme.dimens.spacerMedium))
        }
        AutoResizedText(
            text = timer,
            modifier = Modifier
                .fillMaxWidth()
                .weight(8f)
                .align(Alignment.CenterVertically),
            textStyle = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
        )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            BorderedIcon(icon = R.drawable.ic_plus, onTap = onIncreasedTap)
            Spacer(modifier = Modifier.height(TimerAppTheme.dimens.spacerMedium))
        }
    }
}


@Composable
fun TimerTypeSession(
    modifier: Modifier = Modifier,
    type: TimerTypeEnum,
    onTap: (TimerTypeEnum) -> Unit = {}
){
    val gridColumns = 3
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .height(TimerAppTheme.dimens.spacerLarge),
        columns = GridCells.Fixed(gridColumns),
        horizontalArrangement = Arrangement.spacedBy(TimerAppTheme.dimens.paddingNormal),
        verticalArrangement = Arrangement.spacedBy(TimerAppTheme.dimens.paddingNormal)
    ) {
        items(
            TimerTypeEnum.values(),
            key = { it.title }
        ){
            TimerTypeItem(
                text = it.title ,
                textColor = if (type == it)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.secondary,
                onTap = { onTap(it) }
            )
        }
    }
}

@Preview(
    name = "HomeScreenPreview",
    showBackground = true
)
@Composable
fun HomeScreenPreview(){
    TimerAppTheme {
        HomeScreen()
    }
}