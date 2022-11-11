package com.example.kelilinkseller.core.di

import com.example.kelilinkseller.core.data.repository.AuthRepositoryImpl
import com.example.kelilinkseller.core.data.repository.OrderRepositoryImpl
import com.example.kelilinkseller.core.data.repository.SellerRepositoryImpl
import com.example.kelilinkseller.core.data.repository.StoreRepositoryImpl
import com.example.kelilinkseller.core.domain.repository.AuthRepository
import com.example.kelilinkseller.core.domain.repository.OrderRepository
import com.example.kelilinkseller.core.domain.repository.SellerRepository
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
    abstract fun provideOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository

    @Binds
    abstract fun provideSellerRepository(
        sellerRepositoryImpl: SellerRepositoryImpl
    ): SellerRepository

    @Binds
    abstract fun provideStoreRepository(storeRepositoryImpl: StoreRepositoryImpl): StoreRepository

}