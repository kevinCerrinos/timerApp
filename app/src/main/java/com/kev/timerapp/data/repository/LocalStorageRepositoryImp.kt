package com.kev.timerapp.data.repository

import com.kev.timerapp.data.local.dao.TimerSessionDao
import com.kev.timerapp.domain.model.TimerSessionModel
import com.kev.timerapp.domain.model.toTimerSessionEntity
import com.kev.timerapp.domain.repository.LocalStorageRepository
import javax.inject.Inject

class LocalStorageRepositoryImp
    @Inject
    constructor(
        private val timerSessionDao: TimerSessionDao
    ) : LocalStorageRepository
{
    override suspend fun saveTimerSession(timerSessionModel: TimerSessionModel): Boolean {
        try {
            val result = timerSessionDao.insertTimerSession(timerSessionModel.toTimerSessionEntity())
            return result.toInt() != -1
        }catch (e : Exception){
            throw e
        }
    }

    override suspend fun getTimerSessionByDate(date: String): TimerSessionModel {
        try {
            var timerValue: Long = 0
            var rounds = 0
            timerSessionDao.getTimerSessionByDate(date).map {
                timerValue += it.value ?: 0
                rounds += 1
            }

            return TimerSessionModel(
                date = date,
                value = timerValue,
                round = rounds
            )
        }catch (e: Exception){
            throw e
        }
    }

}