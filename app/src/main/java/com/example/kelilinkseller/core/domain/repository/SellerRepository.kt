package com.example.kelilinkseller.core.domain.repository

import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import kotlinx.coroutines.flow.Flow

interface SellerRepository {
    fun setFcmToken(token: String)
    fun getFcmToken(): String

    fun updatePassword(oldPassword: String, newPassword: String): Flow<Resource<Seller>>
}