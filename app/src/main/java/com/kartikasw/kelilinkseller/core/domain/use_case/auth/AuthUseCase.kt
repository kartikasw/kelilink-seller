package com.kartikasw.kelilinkseller.core.domain.use_case.auth

import android.net.Uri
import com.kartikasw.kelilinkseller.core.domain.Resource
import com.kartikasw.kelilinkseller.core.domain.model.Seller
import com.kartikasw.kelilinkseller.core.domain.model.Store
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    fun register(email: String, password: String, seller: Seller, store: Store, uri: Uri): Flow<Resource<Unit>>
    fun logIn(email: String, password: String, fcmToken: String): Flow<Resource<Unit>>
    fun resetPassword(email: String): Flow<Resource<Unit>>
}