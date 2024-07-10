package com.kev.timerapp.presentation.home

import android.annotation.SuppressLint
import android.icu.util.Calendar
import android.os.CountDownTimer
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kev.timerapp.core.Constants.Companion.ONE_HOUR_IN_MIN
import com.kev.timerapp.core.Constants.Companion.ONE_MIN_IN_MILLIS
import com.kev.timerapp.core.Constants.Companion.ONE_MIN_IN_SEC
import com.kev.timerapp.core.Constants.Companion.ONE_SEC_IN_MILLIS
import com.kev.timerapp.domain.model.Resource
import com.kev.timerapp.domain.model.TimerSessionModel
import com.kev.timerapp.domain.model.TimerTypeEnum
import com.kev.timerapp.domain.usecase.GetTimerSessionByDateUseCase
import com.kev.timerapp.domain.usecase.SaveTimerSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val getTimerSessionByDateUseCase: GetTimerSessionByDateUseCase,
    private val saveTimerSessionUseCase: SaveTimerSessionUseCase
) : ViewModel() {
    private lateinit var timer: CountDownTimer
    private var isTimerActive: Boolean = false

    private val _timerValue = mutableLongStateOf(TimerTypeEnum.FOCUS.timeToMillis())
    val timerValueState = _timerValue

    private val _timerTypeState = mutableStateOf(TimerTypeEnum.FOCUS)
    val timerTypeState = _timerTypeState

    private val _roundsState = mutableIntStateOf(0)
    val roundsState = _roundsState

    private val _todayTimeState = mutableLongStateOf(0)
    val todayTimeState = _todayTimeState

    private var _sessionTimerValue: Long = 0

    fun onStartTimer(){
        viewModelScope.launch {
            timer = object : CountDownTimer(
                _timerValue.longValue,
                ONE_SEC_IN_MILLIS
            ){
                override fun onTick(millisUntilFinished: Long) {
                    _timerValue.longValue = millisUntilFinished
                    _todayTimeState.longValue += ONE_SEC_IN_MILLIS
                    _sessionTimerValue += ONE_SEC_IN_MILLIS
                }

                override fun onFinish() {
                    onCancelTimer()
                }
            }

            timer.start().also {
                if (!isTimerActive) _roundsState.intValue += 1
                _sessionTimerValue = 0
                isTimerActive = true
            }
        }
    }

    fun onCancelTimer(reset:Boolean = false) {
        try {
            saveTimerSession()
            timer.cancel()
        }catch (_ : UninitializedPropertyAccessException){

        }
        if (!isTimerActive || reset ){
            _timerValue.longValue = _timerTypeState.value.timeToMillis()
        }
        isTimerActive = false
    }

    private fun onResetTime(){
        if (isTimerActive){
            onCancelTimer()
            onStartTimer()
        }
    }

    fun onUpdateType(timerType: TimerTypeEnum){
        _timerTypeState.value = timerType
        onCancelTimer(true)
    }

    fun onIncreaseTime(){
        _timerValue.longValue += ONE_MIN_IN_MILLIS
        onResetTime()
    }

    fun onDecreaseTime(){
        _timerValue.longValue -= ONE_MIN_IN_MILLIS
        onResetTime()
        if (_timerValue.longValue < 0){
            onCancelTimer()
        }
    }

    fun getTimerSessionByDate(){
        getTimerSessionByDateUseCase(date = getCurrentDate()).onEach { result ->
            if (result is Resource.Success){
                _roundsState.intValue = result.data?.round ?: 0
                _todayTimeState.longValue = result.data?.value ?: 0
            }
        }.launchIn(viewModelScope)
    }

    private fun saveTimerSession(){
        val session = TimerSessionModel(
            date = getCurrentDate(),
            value = _sessionTimerValue
        )
        saveTimerSessionUseCase(timerSessionModel = session).onEach { result ->
            when(result){
                is Resource.Error -> {_sessionTimerValue = 0}
                is Resource.Loading -> {}
                is Resource.Success -> {}
            }
        }.launchIn(viewModelScope)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val currentDate = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MMMM-yyyy")
        return formatter.format(currentDate)
    }

    @SuppressLint("DefaultLocale")
    fun millisToMinutes(value:Long):String {
        val totalSeconds = (value / ONE_SEC_IN_MILLIS)
        val minutes = (totalSeconds / ONE_MIN_IN_SEC).toInt()
        val seconds = (totalSeconds % ONE_MIN_IN_SEC).toInt()
        return String.format("%02d:%02d",minutes,seconds)
    }

    @SuppressLint("DefaultLocale")
    fun millisToHours(value: Long):String{
        val totalSeconds = value / ONE_SEC_IN_MILLIS
        val seconds = totalSeconds % ONE_MIN_IN_SEC
        val totalMinutes = (totalSeconds / ONE_MIN_IN_SEC).toInt()
        val hours = (totalMinutes / ONE_HOUR_IN_MIN)
        val minutes = (totalMinutes % ONE_HOUR_IN_MIN)
        return if(totalMinutes <= ONE_HOUR_IN_MIN){
            String.format("%02dm %02ds", minutes, seconds)
        }else {
            String.format("%02dh %02dm",hours,minutes)
        }
    }
}