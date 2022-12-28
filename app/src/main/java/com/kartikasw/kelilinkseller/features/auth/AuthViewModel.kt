package com.kartikasw.kelilinkseller.features.auth

import androidx.lifecycle.ViewModel
import com.kartikasw.kelilinkseller.core.domain.use_case.seller.SellerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sellerUseCase: SellerUseCase
): ViewModel() {

    fun setFcmToken(token: String) =
        sellerUseCase.setFcmToken(token)

    fun getFcmToken() =
        sellerUseCase.getFcmToken()
}