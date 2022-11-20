package com.example.kelilinkseller.core.data.repository

import android.net.Uri
import com.example.kelilinkseller.core.data.helper.Response
import com.example.kelilinkseller.core.data.source.remote.RemoteDataSource
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.model.Store
import com.example.kelilinkseller.core.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val remote: RemoteDataSource
): AuthRepository {

    override fun register(email: String, password: String, seller: Seller, store: Store, uri: Uri): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.register(email, password, seller, store, uri).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.logIn(email, password, fcmToken).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }

    override fun resetPassword(email: String): Flow<Resource<Unit>> =
        flow {
            emit(Resource.Loading())
            when(val response = remote.resetPassword(email).first()) {
                is Response.Success -> {
                    emit(Resource.Success(null))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                else -> {}
            }
        }
}