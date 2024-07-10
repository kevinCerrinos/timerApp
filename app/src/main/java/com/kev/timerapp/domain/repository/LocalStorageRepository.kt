package com.kev.timerapp.domain.repository

import com.kev.timerapp.domain.model.TimerSessionModel

interface LocalStorageRepository {
    suspend fun saveTimerSession(timerSessionModel: TimerSessionModel): Boolean
    suspend fun getTimerSessionByDate(date:String): TimerSessionModel
}