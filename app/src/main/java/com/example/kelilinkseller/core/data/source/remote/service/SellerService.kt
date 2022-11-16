package com.example.kelilinkseller.core.data.source.remote.service

import com.example.kelilinkseller.core.data.helper.Response
import com.example.kelilinkseller.core.data.source.remote.response.SellerResponse
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SellerService @Inject constructor(): FirebaseService() {
    fun updatePassword(oldPassword: String, newPassword: String): Flow<Response<SellerResponse>> =
        reAuthenticate(oldPassword, newPassword)
}