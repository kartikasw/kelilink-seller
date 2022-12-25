package com.example.kelilinkseller.core.domain.use_case.seller

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface SellerUseCase {
    fun getUser(): FirebaseUser

    fun setFcmToken(token: String)
    fun getFcmToken(): String

    fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<Seller>>
}