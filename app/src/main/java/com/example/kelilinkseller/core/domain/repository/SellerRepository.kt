package com.example.kelilinkseller.core.domain.repository

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface SellerRepository {
    fun getUser(): FirebaseUser

    fun setFcmToken(token: String)
    fun getFcmToken(): String

    fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<Seller>>
}