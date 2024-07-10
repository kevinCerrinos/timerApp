package com.kev.timerapp.di

import android.content.Context
import androidx.room.Room
import com.kev.timerapp.data.local.AppDataBase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext appContext: Context): AppDataBase{
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java, "focus_timer_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideTimerSessionDao(db: AppDataBase) = db.timerSessionDao()
}