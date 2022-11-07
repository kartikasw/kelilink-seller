package com.example.kelilinkseller.core.di

import com.example.kelilinkseller.core.data.repository.AuthRepositoryImpl
import com.example.kelilinkseller.core.data.repository.StoreRepositoryImpl
import com.example.kelilinkseller.core.domain.repository.AuthRepository
import com.example.kelilinkseller.core.domain.repository.StoreRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun provideAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun provideStoreRepository(storeRepositoryImpl: StoreRepositoryImpl): StoreRepository

}