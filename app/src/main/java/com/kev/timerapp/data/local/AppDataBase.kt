package com.kev.timerapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kev.timerapp.data.local.dao.TimerSessionDao
import com.kev.timerapp.data.local.entity.TimerSessionEntity

@Database(entities = [TimerSessionEntity::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun timerSessionDao(): TimerSessionDao
}