package com.kartikasw.kelilinkseller.core.data.repository

import com.kartikasw.kelilinkseller.core.data.helper.Response
import com.kartikasw.kelilinkseller.core.data.mapper.toModel
import com.kartikasw.kelilinkseller.core.data.source.local.LocalDataSource
import com.kartikasw.kelilinkseller.core.data.source.remote.RemoteDataSource
import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.core.domain.model.Seller
import com.kartikasw.kelilinkseller.core.domain.repository.SellerRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SellerRepositoryImpl @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
): SellerRepository {

    override fun getUser(): FirebaseUser =
        remote.getUser()!!

    override fun setFcmToken(token: String) {
        local.setFcmToken(token)
    }

    override fun getFcmToken(): String =
        local.getFcmToken()!!

    override fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<Seller>> =
        flow {
            emit(Resource.Loading())
            when (val response = remote.updatePassword(oldPassword, newPassword).first()) {
                is Response.Success -> {
                    emit(Resource.Success(response.data.toModel()))
                }
                is Response.Error -> {
                    emit(Resource.Error(response.errorMessage))
                }
                is Response.Empty -> {
                    emit(Resource.Success(null))
                }
            }
        }

}