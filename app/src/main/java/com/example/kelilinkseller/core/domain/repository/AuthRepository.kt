package com.example.kelilinkseller.core.domain.repository

import android.net.Uri
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun register(email: String, password: String, seller: Seller, store: Store, uri: Uri): Flow<Resource<Unit>>
    fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>>
    fun resetPassword(email: String): Flow<Resource<Unit>>
}