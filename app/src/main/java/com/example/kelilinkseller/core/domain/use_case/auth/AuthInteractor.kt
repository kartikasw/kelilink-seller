package com.example.kelilinkseller.core.domain.use_case.auth

import android.net.Uri
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthInteractor @Inject constructor(private val authRepository: AuthRepository) : AuthUseCase {
    override fun register(email: String, password: String, seller: Seller, store: Store, uri: Uri): Flow<Resource<Unit>> =
        authRepository.register(email, password, seller, store, uri)

    override fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>> =
        authRepository.logIn(email, password, fcmToken)

    override fun resetPassword(email: String): Flow<Resource<Unit>> =
        authRepository.resetPassword(email)
}