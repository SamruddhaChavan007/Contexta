package com.example.contexta.di

import com.example.contexta.data.profile.ProfileLocalRepository
import com.example.contexta.data.profile.ProfileLocalRepositoryImpl
import com.example.contexta.profile.data.repository.ProfileRepository
import com.example.contexta.profile.data.repository.ProfileRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ProfileModule {

    @Binds
    @Singleton
    abstract fun bindProfileLocalRepository(impl: ProfileLocalRepositoryImpl): ProfileLocalRepository

    @Binds
    @Singleton
    abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}
