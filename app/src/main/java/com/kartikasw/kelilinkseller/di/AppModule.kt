package com.kartikasw.kelilinkseller.di

import com.kartikasw.kelilinkseller.core.domain.use_case.auth.AuthInteractor
import com.kartikasw.kelilinkseller.core.domain.use_case.auth.AuthUseCase
import com.kartikasw.kelilinkseller.core.domain.use_case.order.OrderInteractor
import com.kartikasw.kelilinkseller.core.domain.use_case.order.OrderUseCase
import com.kartikasw.kelilinkseller.core.domain.use_case.seller.SellerInteractor
import com.kartikasw.kelilinkseller.core.domain.use_case.seller.SellerUseCase
import com.kartikasw.kelilinkseller.core.domain.use_case.store.StoreInteractor
import com.kartikasw.kelilinkseller.core.domain.use_case.store.StoreUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class AppModule {

    @Binds
    @ViewModelScoped
    abstract fun provideAuthUseCase(authInteractor: AuthInteractor): AuthUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideOrderUseCase(orderInteractor: OrderInteractor): OrderUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideSellerUseCase(sellerInteractor: SellerInteractor): SellerUseCase

    @Binds
    @ViewModelScoped
    abstract fun provideStoreUseCase(storeInteractor: StoreInteractor): StoreUseCase

}