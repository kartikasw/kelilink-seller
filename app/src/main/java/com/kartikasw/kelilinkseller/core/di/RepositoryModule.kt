package com.kartikasw.kelilinkseller.core.di

import com.kartikasw.kelilinkseller.core.data.repository.AuthRepositoryImpl
import com.kartikasw.kelilinkseller.core.data.repository.OrderRepositoryImpl
import com.kartikasw.kelilinkseller.core.data.repository.SellerRepositoryImpl
import com.kartikasw.kelilinkseller.core.data.repository.StoreRepositoryImpl
import com.kartikasw.kelilinkseller.core.domain.repository.AuthRepository
import com.kartikasw.kelilinkseller.core.domain.repository.OrderRepository
import com.kartikasw.kelilinkseller.core.domain.repository.SellerRepository
import com.kartikasw.kelilinkseller.core.domain.repository.StoreRepository
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