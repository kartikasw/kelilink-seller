package com.example.kelilinkseller.core.domain.use_case.auth

import android.net.Uri
import com.example.kelilinkseller.core.domain.Resource
import com.example.kelilinkseller.core.domain.model.Seller
import com.example.kelilinkseller.core.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    fun register(email: String, password: String, seller: Seller, store: Store, uri: Uri): Flow<Resource<Unit>>
    fun logIn(email: String, password: String): Flow<Resource<Unit>>
}