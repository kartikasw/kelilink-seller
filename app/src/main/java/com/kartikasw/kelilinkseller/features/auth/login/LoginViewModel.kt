package com.kartikasw.kelilinkseller.features.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kartikasw.kelilinkseller.core.domain.use_case.auth.AuthUseCase
import com.kartikasw.kelilinkseller.core.domain.use_case.seller.SellerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val sellerUseCase: SellerUseCase
): ViewModel() {

    fun logIn(email: String, password: String, fcmToken: String) =
        authUseCase.logIn(email, password, fcmToken).asLiveData()

    fun getFcmToken() =
        sellerUseCase.getFcmToken()

}