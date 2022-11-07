package com.example.kelilinkseller.di

import com.example.kelilinkseller.core.domain.use_case.auth.AuthInteractor
import com.example.kelilinkseller.core.domain.use_case.auth.AuthUseCase
import com.example.kelilinkseller.core.domain.use_case.store.StoreInteractor
import com.example.kelilinkseller.core.domain.use_case.store.StoreUseCase
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
    abstract fun provideStoreUseCase(storeInteractor: StoreInteractor): StoreUseCase

}