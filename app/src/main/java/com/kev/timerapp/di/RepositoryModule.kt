package com.kev.timerapp.di

import com.kev.timerapp.data.repository.LocalStorageRepositoryImp
import com.kev.timerapp.domain.repository.LocalStorageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindLocalStorageRepository(
        localStorageRepositoryImp: LocalStorageRepositoryImp
    ) : LocalStorageRepository
}